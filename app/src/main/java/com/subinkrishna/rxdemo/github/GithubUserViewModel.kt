package com.subinkrishna.rxdemo.github

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * @author Subinkrishna Gopi
 */
class GithubUserViewModel : ViewModel() {

    companion object {
        // Log tag
        private const val TAG = "GithubUserViewModel"
    }

    // View mode enum
    enum class ViewMode { NONE, PROGRESS, CONTENT, ERROR }

    var username: String? = null
        set(value) {
            if (value.isNullOrBlank()) {
                viewModeLive.value = ViewMode.ERROR
            }
            else if (field != value) {
                field = value
                viewModeLive.value = ViewMode.PROGRESS
                inputLive.value = value
            }
        }

    private val inputLive = MutableLiveData<String>()
    private val disposables by lazy { CompositeDisposable() }

    // Public LiveData instances
    val userLive : LiveData<Pair<User, List<Repo>?>> = Transformations.switchMap(inputLive) { createUserLiveData(it) }
    val viewModeLive = MutableLiveData<ViewMode>()

    init {
        viewModeLive.value = ViewMode.PROGRESS
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }


    // Internal methods

    /**
     * Creates a user [MutableLiveData]
     *
     * @param username
     * @return live data
     */
    private fun createUserLiveData(username: String): MutableLiveData<Pair<User, List<Repo>?>> {
        val profile = Github.user(username)
                // For testing
                //.delay(5, TimeUnit.SECONDS)
        val repos = Github.repos(username)
                // Return an empty list if fails to fetch the repos
                //.onErrorReturn { emptyList() }

        return MutableLiveData<Pair<User, List<Repo>?>>().also { live ->
            // Combine profile & repo calls' responses
            val zipped = Single.zip(profile, repos, BiFunction<User, List<Repo>?, Pair<User, List<Repo>?>> { user, repo ->
                Pair(user, repo)
            })

            val disposable = zipped
                    // Do the work on IO thread
                    .subscribeOn(Schedulers.io())
                    // Observe on the UI thread
                    .observeOn(AndroidSchedulers.mainThread())
                    // And subscribe
                    .subscribe(
                            // On success
                            { pair ->
                                // Update the LiveData
                                live.value = pair
                                // Show the contents
                                viewModeLive.value = ViewMode.CONTENT
                            },
                            // On error
                            { error ->
                                // Something went wrong. Show error
                                Log.e(TAG, "Error: ${error.message}")
                                viewModeLive.value = ViewMode.ERROR
                            })

            disposables.add(disposable)
        }
    }
}