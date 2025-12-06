package com.example.flickrapp2.ui

import com.example.flickrapp2.data.model.FlickrPhoto
import com.example.flickrapp2.data.model.FlickrResponse
import com.example.flickrapp2.data.model.PhotosPage
import com.example.flickrapp2.data.remote.FlickrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FlickrRepository
    private lateinit var viewModel: PhotosViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock<FlickrRepository>()
        viewModel = PhotosViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun recentPhotos_success_updatesPhotosAndClearsError() = runTest {
        val photos = List(3) { FlickrPhoto(
            id = it.toString(),
            secret = "secret",
            server = "server",
            title = "title"
        ) }
        val response = FlickrResponse(PhotosPage(
            page = 1,
            pages = 1,
            perPage = 30,
            total = 30,
            photo = photos,
        ))
        whenever(repository.recentPhotos(1))
            .thenReturn(response)

        viewModel.loadFirstPage()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(3, state.items.size)
        assertEquals(photos, state.items)
    }

    @Test
    fun recentPhotos_failure_setsErrorState() = runTest {
        whenever(repository.recentPhotos(any(), any()))
            .thenThrow(RuntimeException("boom"))

        viewModel.loadFirstPage()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun retry_success_loadsPhotos() = runTest {
        val photos = List(3) { FlickrPhoto(
            id = it.toString(),
            secret = "secret",
            server = "server",
            title = "title"
        ) }
        val response = FlickrResponse(PhotosPage(
            page = 1,
            pages = 1,
            perPage = 30,
            total = 30,
            photo = photos,
        ))
        whenever(repository.recentPhotos(1))
            .thenReturn(response)

        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(3, state.items.size)
        assertEquals(photos, state.items)
    }
}