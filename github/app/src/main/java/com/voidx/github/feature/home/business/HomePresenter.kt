package com.voidx.github.feature.home.business

import com.voidx.github.data.dto.GitHubRepositoryDTO
import com.voidx.github.feature.home.HomeContract
import io.reactivex.disposables.CompositeDisposable

class HomePresenter(
    val view: HomeContract.View,
    private val interactor: HomeInteractor,
    private val disposables: CompositeDisposable = CompositeDisposable()
) : HomeContract.Presenter {

    companion object {
        const val FIRST_PAGE = 0
    }

    private var results: MutableList<GitHubRepositoryDTO> = mutableListOf()

    override fun putValues(itemView: HomeContract.ItemView, position: Int) {

        val result = results[position]

        itemView.apply {
            putRepoName(result.name ?: "")
            putRepoDescription(result.description ?: "")
            putStarCount(result.starCount?.toString() ?: "0")
            putForkCount(result.forksCount?.toString() ?: "0")
            putAuthorName(result.owner?.login ?: "")
            putAuthorPhoto(result.owner?.avatar ?: "")
        }
    }

    override fun getResultCount(): Int = results.size

    override fun destroy() {
        disposables.dispose()
    }

    override fun reset() {
        val size = results.size
        results.clear()
        view.resetList(0, size)
        interactor.resetPaging()
    }

    override fun load() {

        showLoading()
        hideResults()
        view.hideEmptyState()
        view.hideError()

        val disposable = interactor
            .searchRepositories()
            .subscribe(
                lambda@{
                    handleSuccess(it)
                },
                lambda@{
                    handleError()
                })
        disposables.add(disposable)
    }

    private fun hideResults() {
        if (interactor.currentPage == FIRST_PAGE) {
            view.hideResults()
        }
    }

    private fun handleSuccess(list: List<GitHubRepositoryDTO>) {
        hideLoading()

        if (interactor.currentPage == FIRST_PAGE && list.isEmpty()) {
            view.showEmptyState()
            return
        }

        if (list.isNotEmpty()) {
            val size = results.size
            results.addAll(list)
            view.showResults(size, list.size)
        }
    }

    private fun handleError() {
        hideLoading()

        if (interactor.currentPage == FIRST_PAGE) {
            view.showError()
        }
    }

    private fun showLoading() {
        if (interactor.currentPage == FIRST_PAGE) {
            view.showLoading()
        } else {
            view.showMoreLoading()
        }
    }

    private fun hideLoading() {
        view.hideLoading()
    }
}