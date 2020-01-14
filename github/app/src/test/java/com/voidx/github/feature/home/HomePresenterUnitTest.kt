package com.voidx.github.feature.home

import com.voidx.github.data.repository.GitHubSearchDataSource
import com.voidx.github.feature.home.HomeObjects.injectError422
import com.voidx.github.feature.home.HomeObjects.injectGenericError
import com.voidx.github.feature.home.HomeObjects.injectSuccessSearchResults
import com.voidx.github.feature.home.business.HomeInteractor
import com.voidx.github.feature.home.business.HomePresenter
import com.voidx.github.util.RxImmediateSchedulerRule
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomePresenterUnitTest {

    @Rule
    @JvmField
    val testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var presenter: HomeContract.Presenter
    private lateinit var interactor: HomeInteractor

    private var view: HomeContract.View = mockk(relaxed = true)
    private var itemView: HomeContract.ItemView = mockk(relaxed = true)
    private var searchDataSource: GitHubSearchDataSource = mockk(relaxed = true)

    @Before
    fun setup() {
        interactor = HomeInteractor(searchDataSource)
        presenter = HomePresenter(view, interactor)
    }

    @Test
    fun `test show search successfully`() {

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        presenter.load()

        verifyAll {
            view.showLoading()
            view.hideEmptyState()
            view.hideError()
            view.hideResults()
            view.hideLoading()
            view.showResults(0, 3)
        }

        verify(exactly = 0) {
            view.showEmptyState()
        }

        verify(exactly = 0) {
            view.showMoreLoading()
        }

        verify(exactly = 0) {
            view.showError()
        }

        assertEquals(3, presenter.getResultCount())
    }

    @Test
    fun `test callers only first page`() {

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        presenter.load()
        presenter.load()

        assertEquals(2, interactor.currentPage)

        verify(exactly = 1) {
            view.hideResults()
        }

        verify(exactly = 1) {
            view.showLoading()
        }
    }

    @Test
    fun `test show successfully list item information`() {
        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        presenter.load()
        presenter.putValues(itemView, 0)

        verifyAll {
            itemView.putRepoName(any())
            itemView.putRepoDescription(any())
            itemView.putStarCount(any())
            itemView.putForkCount(any())
            itemView.putAuthorName(any())
            itemView.putAuthorPhoto(any())
        }

    }

    @Test
    fun `test show only loading more after paging`() {
        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        assertEquals(0, interactor.currentPage)

        presenter.load()

        assertEquals(1, interactor.currentPage)

        presenter.load()

        assertEquals(2, interactor.currentPage)

        presenter.load()

        assertEquals(3, interactor.currentPage)

        verify(exactly = 1) {
            view.showLoading()
        }

        verify(exactly = 2) {
            view.showMoreLoading()
        }

    }

    @Test
    fun `test show error`() {
        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectGenericError()

        presenter.load()

        verifyAll {
            view.showLoading()
            view.hideEmptyState()
            view.hideError()
            view.hideResults()
            view.hideLoading()

            view.showError()
        }
    }

    @Test
    fun `test do not show error after paging`() {
        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        assertEquals(0, interactor.currentPage)

        presenter.load()

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectGenericError()

        assertEquals(1, interactor.currentPage)

        presenter.load()

        verify(exactly = 0) { view.showError() }
    }

    @Test
    fun `test next paging`() {

        val positionStartSlot = slot<Int>()
        val itemCountSlot = slot<Int>()

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        every { view.showResults(capture(positionStartSlot), capture(itemCountSlot)) } just Runs

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        presenter.load()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)
        assertEquals(3, presenter.getResultCount())
        assertEquals(0, positionStartSlot.captured)
        assertEquals(3, itemCountSlot.captured)

        presenter.load()

        assertEquals(2, interactor.currentPage)
        assertEquals(3, interactor.nextPage)
        assertEquals(6, presenter.getResultCount())
        assertEquals(3, positionStartSlot.captured)
        assertEquals(3, itemCountSlot.captured)
    }

    @Test
    fun `test first paging returning empty with 422 error`() {

        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectError422()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        presenter.load()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)
        assertEquals(0, presenter.getResultCount())

        verify(exactly = 0) {
            view.showResults(any(), any())
        }
    }

    @Test
    fun `test second paging returning empty with 422 error`() {

        val positionStartSlot = slot<Int>()
        val itemCountSlot = slot<Int>()

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        every { view.showResults(capture(positionStartSlot), capture(itemCountSlot)) } just Runs

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        presenter.load()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)
        assertEquals(3, presenter.getResultCount())
        assertEquals(0, positionStartSlot.captured)
        assertEquals(3, itemCountSlot.captured)

        every { searchDataSource.searchRepositories(any(), any(), any()) } returns injectError422()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)

        presenter.load()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)
        assertEquals(3, presenter.getResultCount())

        verify(atLeast = 1) {
            view.showResults(any(), any())
        }
    }

    @Test
    fun `test reset paging after list with items`() {
        val positionStartSlot = slot<Int>()
        val itemCountSlot = slot<Int>()

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        every { view.showResults(capture(positionStartSlot), capture(itemCountSlot)) } just Runs

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)

        presenter.load()

        assertEquals(1, interactor.currentPage)
        assertEquals(2, interactor.nextPage)
        assertEquals(3, presenter.getResultCount())
        assertEquals(0, positionStartSlot.captured)
        assertEquals(3, itemCountSlot.captured)

        presenter.reset()

        assertEquals(0, interactor.currentPage)
        assertEquals(1, interactor.nextPage)
        assertEquals(0, presenter.getResultCount())

        verify {
            view.resetList(0, 3)
        }
    }

    @Test
    fun `test show error after reset paging with items`() {

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectSuccessSearchResults()

        assertEquals(0, interactor.currentPage)
        assertEquals(0, presenter.getResultCount())

        presenter.load()

        assertEquals(1, interactor.currentPage)
        assertEquals(3, presenter.getResultCount())

        presenter.reset()

        assertEquals(0, interactor.currentPage)
        assertEquals(0, presenter.getResultCount())

        every {
            searchDataSource.searchRepositories(
                any(),
                any(),
                any()
            )
        } returns injectGenericError()

        presenter.load()

        assertEquals(0, interactor.currentPage)
        assertEquals(0, presenter.getResultCount())

        verify { view.showError() }
    }

}