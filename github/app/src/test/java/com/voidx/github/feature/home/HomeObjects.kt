package com.voidx.github.feature.home

import com.voidx.github.data.dto.GitHubSearchDTO
import com.voidx.github.util.TestUtil
import com.voidx.github.util.TestUtil.Companion.createConnectionErrorObservable
import io.reactivex.Observable

object HomeObjects {

    fun injectSuccessSearchResults(): Observable<GitHubSearchDTO> {
        val values =
            TestUtil.getObject("search_result_200.json", GitHubSearchDTO::class.java)
        return Observable.just(values)
    }

    fun injectError422(): Observable<GitHubSearchDTO> {
        return Observable.error(TestUtil.createGenericError(422))
    }

    fun injectGenericError(): Observable<GitHubSearchDTO> {
        return createConnectionErrorObservable()
    }
}