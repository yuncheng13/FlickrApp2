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
        isRequestInFlight = true
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {

            try {

                val result: FlickrResponse = if (query.isNullOrBlank()) {
                    repo.recentPhotos(page = page, perPage = perPage)
                } else {
                    repo.searchPhotos(query = query, page = page, perPage = perPage)
                }

                val photosPage = result.photos
                val newList =
                    if (page == 1) photosPage.photos else _uiState.value.items + photosPage.photos
                _uiState.value = _uiState.value.copy(
                    items = newList,
                    isLoading = false,
                    page = photosPage.page,
                    lastPage = photosPage.pages,
                    error = null
                )
                isRequestInFlight = false

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onSearch(query: String) {
        loadFirstPage(searchQuery = if (query.isBlank()) null else query)
    }

    fun loadNextPageIfNeeded() {
        val state = _uiState.value
        if (isRequestInFlight) return
        if (state.page >= state.lastPage) return // no more pages
        loadPage(state.page + 1, state.query)
    }

    fun retry() {
        val state = _uiState.value
        loadPage(state.page, state.query)
    }


}
