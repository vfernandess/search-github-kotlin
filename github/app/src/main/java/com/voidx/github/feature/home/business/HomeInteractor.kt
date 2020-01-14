package com.voidx.github.feature.home.business

import com.voidx.github.data.dto.GitHubRepositoryDTO
import com.voidx.github.data.dto.GitHubSearchDTO
import com.voidx.github.data.repository.GitHubSearchDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class HomeInteractor(private val searchDataSource: GitHubSearchDataSource) {

    companion object {
        const val UNPROCESSABLE_ENTITY = 422
        const val LANGUAGE_SEARCH = "language:%s"
    }

    var nextPage: Int = 1
        private set

    val currentPage: Int
        get() = nextPage - 1

    fun searchRepositories(): Observable<List<GitHubRepositoryDTO>> {
        return searchDataSource
            .searchRepositories(LANGUAGE_SEARCH.format("Kotlin"), "stars", nextPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .onErrorResumeNext(this::handleErrorIfNeeded)
            .map {
                increasePaging(it)
                it.items
            }

    }

    fun resetPaging() {
        nextPage = 1
    }

    private fun handleErrorIfNeeded(throwable: Throwable): Observable<GitHubSearchDTO> {
        return when (throwable) {
            is HttpException -> {
                if (throwable.code() == UNPROCESSABLE_ENTITY) {
                    Observable.just(GitHubSearchDTO(emptyList()))
                } else {
                    Observable.error(throwable)
                }
            }
            else -> Observable.error(throwable)
        }
    }

    private fun increasePaging(results: GitHubSearchDTO) {
        takeIf { results.items.isNullOrEmpty().not() }?.apply { nextPage += 1 }
    }

}