package com.subinkrishna.rxdemo.github.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.subinkrishna.rxdemo.R

/**
 * Github user profile activity. Renders the profile & repository details (first 30, doesn't do paging).
 *
 * @author Subinkrishna Gopi
 */
class GithubUserActivity : AppCompatActivity() {

    companion object {
        // Log tag
        private const val TAG = "GithubUserActivity"

        // Keys
        private const val KEY_USERNAME = "GithubUserActivity.KEY_USERNAME"

        @JvmStatic
        fun getStartIntent(context: Context, username: String): Intent {
            return Intent(context, GithubUserActivity::class.java).apply {
                putExtra(KEY_USERNAME, username)
            }
        }
    }

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var statusTextView: TextView

    var username: String? = null

    private var currentViewMode: GithubUserViewModel.ViewMode = GithubUserViewModel.ViewMode.NONE
        set(newMode) {
            if (field != newMode) {
                field = newMode
                Log.d(TAG, "Set view mode: $newMode")
                progressBar.isVisible = newMode == GithubUserViewModel.ViewMode.PROGRESS
                recyclerView.isVisible = newMode == GithubUserViewModel.ViewMode.CONTENT
                statusTextView.isVisible = newMode == GithubUserViewModel.ViewMode.ERROR
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user)

        progressBar = findViewById(R.id.progress)
        statusTextView = findViewById(R.id.status)
        recyclerView = findViewById<RecyclerView>(R.id.list).apply {
            adapter = GithubUserRepoAdapter()
            layoutManager = LinearLayoutManager(this@GithubUserActivity)
            addItemDecoration(DividerItemDecoration(this@GithubUserActivity, DividerItemDecoration.VERTICAL))
        }

        // Get the username
        username = intent?.getStringExtra(KEY_USERNAME)

        // Get ViewModel
        ViewModelProviders.of(this)[GithubUserViewModel::class.java].apply {
            // Set input
            username = this@GithubUserActivity.username

            // Observe user data
            userLive.observe(this@GithubUserActivity, Observer { pair ->
                (recyclerView.adapter as GithubUserRepoAdapter).update(pair?.first, pair?.second)
            })

            // Observe view state changes
            viewModeLive.observe(this@GithubUserActivity, Observer { currentViewMode = it!! })
        }
    }
}