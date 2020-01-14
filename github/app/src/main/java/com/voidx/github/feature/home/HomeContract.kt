package com.voidx.github.feature.home

interface HomeContract {

    interface View {

        fun showLoading()

        fun hideLoading()

        fun showMoreLoading()

        fun showResults(positionStart: Int, itemCount: Int)

        fun hideResults()

        fun showEmptyState()

        fun hideEmptyState()

        fun showError()

        fun hideError()

        fun resetList(positionStart: Int, itemCount: Int)
    }

    interface ItemView {

        fun putRepoName(name: String)

        fun putRepoDescription(description: String)

        fun putForkCount(forkCount: String)

        fun putStarCount(starCount: String)

        fun putAuthorPhoto(photo: String)

        fun putAuthorName(name: String)
    }

    interface Presenter {

        fun load()

        fun reset()

        fun putValues(itemView: ItemView, position: Int)

        fun getResultCount(): Int

        fun destroy()
    }

}