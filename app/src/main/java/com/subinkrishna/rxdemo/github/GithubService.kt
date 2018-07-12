package com.subinkrishna.rxdemo.github

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface definition for Github API (https://developer.github.com/v3/)
 *
 * @author Subinkrishna Gopi
 */
interface GithubService {
    /**
     * Fetches Github user profile
     * API: https://api.github.com/users/{USERNAME}
     *
     * @param username
     */
    @GET("/users/{username}")
    fun user(@Path(value = "username") username: String): Single<User>

    /**
     * Fetches Github user's public repositories
     * API: https://api.github.com/users/{USERNAME}/repos
     *
     * @param username
     */
    @GET("/users/{username}/repos")
    fun repos(@Path(value = "username") username: String): Single<List<Repo>?>
}

/** User */
data class User(@SerializedName("login") val username: String,
                @SerializedName("avatar_url") val avatarUrl: String? = null,
                @SerializedName("name") val name: String = "",
                @SerializedName("bio") val bio: String? = null)

/** Repo */
data class Repo(@SerializedName("id") val id: Double,
                @SerializedName("name") val name: String,
                @SerializedName("description") val description: String? = null,
                @SerializedName("fork") val isForked: Boolean)