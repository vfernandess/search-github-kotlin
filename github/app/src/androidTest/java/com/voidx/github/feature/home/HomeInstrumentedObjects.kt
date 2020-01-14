package com.voidx.github.feature.home

import com.voidx.github.util.UiTestUtil.readFixture
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object HomeInstrumentedObjects {

    fun injectsSearchResultsList(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(readFixture("search_result_200.json"))
            }
        }
    }

    fun injectsEmptySearchResultsList(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(readFixture("github_users_list_empty_200.json"))
            }
        }
    }

    fun injectsUnprocessableEntityError(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(422).setBody(readFixture("generic_error.json"))
            }
        }
    }

    fun injectsServerError(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().setResponseCode(500).setBody(readFixture("generic_error.json"))
            }
        }
    }

}