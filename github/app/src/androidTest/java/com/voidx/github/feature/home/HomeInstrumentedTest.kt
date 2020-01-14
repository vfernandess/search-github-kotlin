package com.voidx.github.feature.home

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import com.voidx.github.feature.home.view.HomeActivity
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeInstrumentedTest {

    private val mockWebServer  = MockWebServer().apply {
        try {
            start(8080)
        } catch (e: Exception) {
            Log.e("MockWebServer", e.message, e)
            e.printStackTrace()
        }
    }

    private lateinit var robot: HomeInstrumentedRobot

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivity::class.java, false, false)

    @Rule
    @JvmField
    var serverRule = InstrumentedTestRequestMatcherRule(mockWebServer)

    @Before
    fun setUp() {
        robot = HomeInstrumentedRobot(serverRule, activityRule)
    }

    @After
    fun finish() {
        activityRule.finishActivity()
    }

    @Test
    fun test_show_successfully_search_results() {
        robot
            .withSuccessfulResponses()
            .start()
            .checkEmptyStateWasNotDisplayed()
            .checkErrorWasNotDisplayed()
            .checkLoadingWasNotDisplayed()
            .checkSearchResultWasDisplayed()
            .checkSearchResultValuesWasDisplayed()
    }

    @Test
    fun test_show_successfully_more_search_results() {
        robot
            .withSuccessfulResponses()
            .start()
            .checkErrorWasNotDisplayed()
            .checkEmptyStateWasNotDisplayed()
            .checkSearchResultWasDisplayed()
            .checkSearchResultsAmount(7)
            .checkSearchResultValuesWasDisplayed()
            .loadMore()
            .checkSearchResultWasDisplayed()
            .checkSearchResultsAmount(14)
    }

    @Test
    fun test_show_server_error() {
        robot
            .withServerErrorResponse()
            .start()
            .checkSearchResultWasNotDisplayed()
            .checkErrorWasDisplayed()
    }

    @Test
    fun test_do_not_show_error_after_paging() {
        robot
            .withSuccessfulResponses()
            .start()
            .checkSearchResultWasDisplayed()
            .checkSearchResultsAmount(7)
            .withServerErrorResponse()
            .loadMore()
            .checkErrorWasNotDisplayed()
            .checkSearchResultsAmount(7)
    }

    @Test
    fun test_do_not_show_more_items_after_422_error() {
        robot
            .withSuccessfulResponses()
            .start()
            .checkSearchResultWasDisplayed()
            .checkSearchResultsAmount(7)
            .withServerUnprocessableEntityErrorResponse()
            .loadMore()
            .checkSearchResultsAmount(7)
    }
}