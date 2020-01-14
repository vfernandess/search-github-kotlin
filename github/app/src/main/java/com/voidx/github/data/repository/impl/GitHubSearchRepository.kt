package com.voidx.github.data.repository.impl

import com.voidx.github.data.dto.GitHubSearchDTO
import com.voidx.github.data.repository.GitHubSearchDataSource
import io.reactivex.Observable

class GitHubSearchRepository(private val remoteDataSource: GitHubSearchDataSource): GitHubSearchDataSource {

    override fun searchRepositories(
        query: String,
        sort: String,
        page: Int
    ): Observable<GitHubSearchDTO> {
        return remoteDataSource.searchRepositories(query, sort, page).cache()
    }
}