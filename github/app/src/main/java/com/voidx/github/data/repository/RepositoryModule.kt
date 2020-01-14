package com.voidx.github.data.repository

import com.voidx.github.data.repository.impl.GitHubSearchRepository
import com.voidx.github.data.repository.impl.remote.GitHubSearchRemoteDataSource
import org.koin.dsl.module

val repositoryModule = module {

    factory<GitHubSearchDataSource> {
        GitHubSearchRepository(
            GitHubSearchRemoteDataSource(get())
        )
    }
}