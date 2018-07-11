package com.subinkrishna.rxdemo.github

import com.subinkrishna.rxdemo.BuildConfig
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A high-level wrapper to [GithubService]
 *
 * @author Subinkrishna Gopi
 * @see GithubService
 */
class Github {
    
    companion object {
        // Log tag
        private const val TAG = "Github"

        // Constants
        const val BASE_URL = "https://api.github.com"

        // HTTP client builder
        private val httpClientBuilder: OkHttpClient.Builder by lazy {
            OkHttpClient.Builder().apply {
                // Add common headers & auth token to all requests
                addInterceptor { chain ->
                    val req = chain.request().newBuilder()
                            .addHeader("User-Agent", "RxDemo-App")
                            .addHeader("Authorization", BuildConfig.GITHUB_ACCESS_TOKEN)
                            .build()
                    chain.proceed(req)
                }
                // Add logging interceptor
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                })
                // Timeouts
                connectTimeout(10, TimeUnit.SECONDS)
                readTimeout(2, TimeUnit.SECONDS)
            }
        }

        // HTTP client
        private val httpClient: OkHttpClient by lazy { httpClientBuilder.build() }

        // Retrofit instance
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build()
        }

        // Singleton GithubService instance
        private val service by lazy { retrofit.create(GithubService::class.java) }


        // Public methods

        /**
         * Fetches the userLive details
         *
         * @param username - Github userLive username
         */
        @JvmStatic
        fun user(username: String): Single<User> = service.user(username)

        /**
         * Fetches the userLive reposLive
         *
         * @param username - Github userLive username
         */
        @JvmStatic
        fun repos(username: String): Single<List<Repo>?> = service.repos(username)
    }
}