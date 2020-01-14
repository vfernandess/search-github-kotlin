package com.voidx.github.data.network.api

import com.voidx.github.data.dto.GitHubSearchDTO
import com.voidx.github.data.dto.GitHubUserDTO
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {

    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String,
                           @Query("sort") sort: String,
                           @Query("page") page: Int) : Observable<GitHubSearchDTO>

    @GET("/user/{login}")
    fun getUserDetail(@Path("login") login: String): Observable<GitHubUserDTO>

}