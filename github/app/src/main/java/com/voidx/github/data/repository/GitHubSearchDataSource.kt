package com.voidx.github.data.repository

import com.voidx.github.data.dto.GitHubSearchDTO
import io.reactivex.Observable

interface GitHubSearchDataSource {

    fun searchRepositories(query: String,
                           sort: String,
                           page: Int): Observable<GitHubSearchDTO>
}