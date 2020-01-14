package com.voidx.github.feature.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voidx.github.R
import com.voidx.github.feature.home.HomeContract


class HomeSearchResultsAdapter(private val presenter: HomeContract.Presenter): RecyclerView.Adapter<HomeSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeSearchResultViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)

        return HomeSearchResultViewHolder(view)
    }

    override fun getItemCount(): Int = presenter.getResultCount()

    override fun onBindViewHolder(holder: HomeSearchResultViewHolder, position: Int) {
        presenter.putValues(holder, position)
    }
}

