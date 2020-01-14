package com.voidx.github.feature.home

import com.voidx.github.data.repository.GitHubSearchDataSource
import com.voidx.github.feature.home.HomeObjects.injectError422
import com.voidx.github.feature.home.HomeObjects.injectGenericError
import com.voidx.github.feature.home.HomeObjects.injectSuccessSearchResults
import com.voidx.github.feature.home.business.HomeInteractor
import com.voidx.github.util.RxImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class HomeInteractorUnitTest {

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var interactor: HomeInteractor

    private var searchDataSource: GitHubSearchDataSource = mockk(relaxed = true)

    @Before
    fun setup() {
        interactor = HomeInteractor(searchDataSource)
    }

    @Test
    fun `test search successfully returns items`() {

        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectSuccessSearchResults()

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 3 }
    }

    @Test
    fun `test search returns empty items on 422 error`() {

        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectError422()

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun `test search returns error`() {

        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectGenericError()

        interactor
            .searchRepositories()
            .test()
            .assertError { it is HttpException }
    }

    @Test
    fun `test paging not increase if empty list is returned`() {
        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectError422()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.isEmpty() }

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)
    }

    @Test
    fun `test paging not increase if throws an error`() {
        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectGenericError()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        interactor
            .searchRepositories()
            .test()
            .assertError { it is HttpException }

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)
    }

    @Test
    fun `test next paging`() {
        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectSuccessSearchResults()

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 3 }

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)
    }

    @Test
    fun `test double next paging`() {
        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectSuccessSearchResults()

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 3 }

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 3 }

        assertEquals(2, interactor.currentPage)
        assertEquals(3, interactor.nextPage)
    }

    @Test
    fun `test reset paging`() {
        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectSuccessSearchResults()

        interactor
            .searchRepositories()
            .test()
            .assertNoErrors()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)

        interactor.resetPaging()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)
    }
}