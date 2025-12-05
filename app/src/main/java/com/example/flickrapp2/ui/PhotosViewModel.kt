package com.example.flickrapp2.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrapp2.data.model.FlickrPhoto
import com.example.flickrapp2.data.model.FlickrResponse
import com.example.flickrapp2.data.remote.FlickrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val items: List<FlickrPhoto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val lastPage: Int = Int.MAX_VALUE,
    val query: String? = null,
)

class PhotosViewModel(private val repo: FlickrRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private val perPage = 30
    private var isRequestInFlight = false

    init {
        loadFirstPage() // initial state: show recent (query = null)
    }

    fun loadFirstPage(searchQuery: String? = null) {
        _uiState.value = UiState(isLoading = true, query = searchQuery, items = emptyList(), page = 1)
        loadPage(1, searchQuery)
    }

    private fun loadPage(page: Int, query: String?) {
        if (isRequestInFlight) return

        isRequestInFlight = true
        updateLoadingState()

        viewModelScope.launch {
            try {
                val response = fetchPhotos(page, query)
                handleSuccessResponse(response, page)
            } catch (e: Exception) {
                handleError(e)
            } finally {
                isRequestInFlight = false
            }
        }
    }

    private suspend fun fetchPhotos(page: Int, query: String?): FlickrResponse {
        return if (query.isNullOrBlank()) {
            repo.recentPhotos(page = page, perPage = perPage)
        } else {
            repo.searchPhotos(query = query, page = page, perPage = perPage)
        }
    }

    private fun handleSuccessResponse(response: FlickrResponse, page: Int) {
        val state = _uiState.value
        val photosPage = response.photos

        val updatedList = if (page == 1) {
            photosPage.photos
        } else {
            state.items + photosPage.photos // Returns a list containing all elements of the original collection and then all elements of the given elements collection.
        }

        _uiState.value = state.copy(
            items = updatedList,
            isLoading = false,
            page = photosPage.page,
            lastPage = photosPage.pages,
            error = null
        )
    }

    private fun handleError(e: Exception) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = e.message
        )
    }

    private fun updateLoadingState() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
    }

    fun onSearch(query: String) {
        loadFirstPage(searchQuery = if (query.isBlank()) null else query)
    }
    fun retry() {
        val state = _uiState.value
        loadPage(state.page, state.query)
    }
    fun loadNextPageIfNeeded() {
        val state = _uiState.value
        if (isRequestInFlight) return
        if (state.page >= state.lastPage) return // no more pages
        loadPage(state.page + 1, state.query)
    }
}
