package com.voidx.github.widget

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessRecyclerViewScrollListener(
    private val visibleThreshold: Int,
    private val direction: Int,
    val onLoadMore: (() -> Unit)?
) : RecyclerView.OnScrollListener() {

    var layoutManager: LinearLayoutManager? = null
    var isLoading = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (layoutManager == null) {
            if (recyclerView.layoutManager is LinearLayoutManager) {
                layoutManager = recyclerView.layoutManager as LinearLayoutManager
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        layoutManager?.let {

            val totalItemCount: Int = it.itemCount
            val visibleItem = visibleItem

            if (isLoading.not() &&
                totalItemCount > 0 &&
                visibleItem > -1 &&
                hasToLoadMore(visibleItem, totalItemCount)
            ) {
                onLoadMore?.invoke()
                isLoading = true
            }
        }
    }

    private val visibleItem: Int
        get() = when (direction) {
            DIRECTION_TOP -> {
                layoutManager?.findFirstVisibleItemPosition() ?: -1
            }
            DIRECTION_BOTTOM -> {
                layoutManager?.findLastVisibleItemPosition() ?: -1
            }
            else -> -1
        }

    private fun hasToLoadMore(visibleItem: Int, totalItemCount: Int): Boolean {
        return when (direction) {
            DIRECTION_TOP -> {
                visibleItem - visibleThreshold <= 0
            }
            DIRECTION_BOTTOM -> {
                totalItemCount <= visibleItem + visibleThreshold
            }
            else -> {
                false
            }
        }
    }

    companion object {
        var DIRECTION_TOP = 0
        var DIRECTION_BOTTOM = 1
    }

}