package com.voidx.github.feature.home

import com.voidx.github.feature.home.business.HomeInteractor
import com.voidx.github.feature.home.business.HomePresenter
import com.voidx.github.feature.home.view.HomeActivity
import com.voidx.github.feature.home.view.HomeSearchResultsAdapter
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var homeModule = module {

    scope(named<HomeActivity>()) {

        scoped { (view: HomeContract.View) ->
            HomePresenter(view, get())
        } bind HomeContract.Presenter::class

        scoped {
            HomeInteractor(get())
        }

        scoped { (presenter: HomeContract.Presenter) ->
            HomeSearchResultsAdapter(presenter)
        }
    }
}