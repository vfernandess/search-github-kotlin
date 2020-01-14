package com.voidx.github.feature.home.view

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.voidx.github.R
import com.voidx.github.feature.home.HomeContract
import kotlinx.android.synthetic.main.search_result_item.view.*

class HomeSearchResultViewHolder(view: View): RecyclerView.ViewHolder(view), HomeContract.ItemView {

    override fun putRepoName(name: String) {
        with(itemView) {
            repo_name.text = name
        }
    }

    override fun putRepoDescription(description: String) {
        with(itemView) {
            repo_description.text = description
        }
    }

    override fun putForkCount(forkCount: String) {
        with(itemView) {
            repo_fork_count.text = forkCount
        }
    }

    override fun putStarCount(starCount: String) {
        with(itemView) {
            repo_star_count.text = starCount
        }
    }

    override fun putAuthorPhoto(photo: String) {
        with(itemView) {
            Glide
                .with(itemView)
                .load(Uri.parse(photo))
                .placeholder(R.drawable.account)
                .transform(CircleCrop())
                .into(author_photo)
        }
    }

    override fun putAuthorName(name: String) {
        with(itemView) {
            author_name.text = name
        }
    }

}