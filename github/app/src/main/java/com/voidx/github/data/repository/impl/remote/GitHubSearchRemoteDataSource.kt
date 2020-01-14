package com.voidx.github.data.repository.impl.remote

import com.voidx.github.data.dto.GitHubSearchDTO
import com.voidx.github.data.network.api.GitHubApi
import com.voidx.github.data.repository.GitHubSearchDataSource
import io.reactivex.Observable

class GitHubSearchRemoteDataSource(private val api: GitHubApi) : GitHubSearchDataSource {

    override fun searchRepositories(
        query: String,
        sort: String,
        page: Int
    ): Observable<GitHubSearchDTO> =
        api.searchRepositories(query, sort, page)
}