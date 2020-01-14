package com.voidx.github.feature.home

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import com.voidx.github.R
import com.voidx.github.feature.home.HomeInstrumentedObjects.injectsEmptySearchResultsList
import com.voidx.github.feature.home.HomeInstrumentedObjects.injectsSearchResultsList
import com.voidx.github.feature.home.HomeInstrumentedObjects.injectsServerError
import com.voidx.github.feature.home.HomeInstrumentedObjects.injectsUnprocessableEntityError
import com.voidx.github.feature.home.view.HomeActivity
import com.voidx.github.util.action.CommonActions.slowSwipeUp
import com.voidx.github.util.matcher.RecyclerViewMatcher.listSizeIs
import com.voidx.github.util.matcher.SwipeRefreshLayoutMatchers.isSwipeRefreshing
import org.hamcrest.core.IsNot.not

class HomeInstrumentedRobot(
    private val serverRule: RequestMatcherRule,
    private val activity: ActivityTestRule<HomeActivity>
) {

    fun start() = apply {
        activity.launchActivity(Intent())
    }

    fun withSuccessfulResponses() = apply {
        serverRule.mockWebServer.dispatcher = injectsSearchResultsList()
    }

    fun withEmptySuccessfulResponses() = apply {
        serverRule.mockWebServer.dispatcher = injectsEmptySearchResultsList()
    }

    fun withServerUnprocessableEntityErrorResponse() = apply {
        serverRule.mockWebServer.dispatcher = injectsUnprocessableEntityError()
    }

    fun withServerErrorResponse() = apply {
        serverRule.mockWebServer.dispatcher = injectsServerError()
    }

    fun checkLoadingWasDisplayed() = apply {
        onView(withId(R.id.loadingContainer)).check(matches(isDisplayed()))
    }

    fun checkLoadingWasNotDisplayed() = apply {
        onView(withId(R.id.loadingContainer)).check(matches(not(isDisplayed())))
    }

    fun checkLoadingMoreWasDisplayed() = apply {
        onView(withId(R.id.refresh)).check(matches(isSwipeRefreshing()))
    }

    fun checkSearchResultWasDisplayed() = apply {
        onView(withId(R.id.search_result)).check(matches(isDisplayed()))
    }

    fun checkSearchResultValuesWasDisplayed() = apply {
        onView(withText("johndoe")).check(matches(isDisplayed()))
        onView(withText("janedoe")).check(matches(isDisplayed()))
        onView(withText("babydoe")).check(matches(isDisplayed()))
    }

    fun checkSearchResultWasNotDisplayed() = apply {
        onView(withId(R.id.search_result)).check(matches(not(isDisplayed())))
    }

    fun checkEmptyStateWasDisplayed() = apply {
        onView(withId(R.id.emptyContainer)).check(matches(isDisplayed()))
    }

    fun checkEmptyStateWasNotDisplayed() = apply {
        onView(withId(R.id.emptyContainer)).check(matches(not(isDisplayed())))
    }

    fun checkErrorWasDisplayed() = apply {
        onView(withId(R.id.errorContainer)).check(matches(isDisplayed()))
    }

    fun checkErrorWasNotDisplayed() = apply {
        onView(withId(R.id.errorContainer)).check(matches(not(isDisplayed())))
    }

    fun swipeToRefreh() = apply {
        onView(withId(R.id.refresh)).perform(swipeDown())
    }

    fun loadMore() = apply {
        onView(withId(R.id.search_result)).perform(slowSwipeUp())
    }

    fun checkSearchResultsAmount(count: Int) = apply {
        onView(withId(R.id.search_result)).check(matches(listSizeIs(count)))
    }
}