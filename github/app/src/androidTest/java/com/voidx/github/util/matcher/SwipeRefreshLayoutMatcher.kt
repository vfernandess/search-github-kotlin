package com.voidx.github.util.matcher

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.hamcrest.Description
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Matcher

object SwipeRefreshLayoutMatchers {

    fun isSwipeRefreshing(): Matcher<View> {

        return object: BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

            override fun describeTo(description: Description?) {
                description?.appendText("is refreshing")
            }

            override fun matchesSafely(item: SwipeRefreshLayout?): Boolean {
                return item?.isRefreshing ?: false
            }
        }
    }
}