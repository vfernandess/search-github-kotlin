package com.voidx.github.feature.home.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.voidx.github.R
import com.voidx.github.feature.home.HomeContract
import com.voidx.github.widget.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class HomeActivity : AppCompatActivity(), HomeContract.View {

    val presenter: HomeContract.Presenter by currentScope.inject { parametersOf(this) }

    val adapter: HomeSearchResultsAdapter by currentScope.inject { parametersOf(presenter) }

    private val endlessRecyclerViewScrollListener = EndlessRecyclerViewScrollListener(
        3,
        EndlessRecyclerViewScrollListener.DIRECTION_BOTTOM,
        onLoadMore = {
            presenter.load()
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        search_result.adapter = adapter
        search_result.addOnScrollListener(endlessRecyclerViewScrollListener)

        refresh.setOnRefreshListener {
            presenter.reset()
            presenter.load()
        }

        presenter.load()
    }

    override fun showLoading() {
        loadingContainer.visibility = VISIBLE
    }

    override fun hideLoading() {
        loadingContainer.visibility = GONE
        refresh.isRefreshing = false
        endlessRecyclerViewScrollListener.isLoading = false
    }

    override fun showMoreLoading() {
        refresh.isRefreshing = true
    }

    override fun showResults(positionStart: Int, itemCount: Int) {
        search_result.visibility = VISIBLE
        adapter.notifyItemRangeInserted(positionStart, itemCount)
    }

    override fun hideResults() {
        search_result.visibility = GONE
    }

    override fun showEmptyState() {
        emptyContainer.visibility = VISIBLE
    }

    override fun hideEmptyState() {
        emptyContainer.visibility = GONE
    }

    override fun showError() {
        errorContainer.visibility = VISIBLE
    }

    override fun hideError() {
        errorContainer.visibility = GONE
    }

    override fun resetList(positionStart: Int, itemCount: Int) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

}
