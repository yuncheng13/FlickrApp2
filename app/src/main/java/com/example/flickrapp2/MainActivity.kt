package com.example.flickrapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.example.flickrapp2.data.model.FlickrPhoto
import com.example.flickrapp2.data.model.getImageUrl
import com.example.flickrapp2.data.remote.FlickrRepository
import com.example.flickrapp2.data.remote.RetrofitClient
import com.example.flickrapp2.ui.PhotosViewModel
import com.example.flickrapp2.ui.UiState
import com.example.flickrapp2.ui.theme.FlickrApp2Theme

class MainActivity : ComponentActivity() {
    private val flickrApiKey = "a0222db495999c951dc33702500fdc4d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = FlickrRepository(RetrofitClient.api, flickrApiKey)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PhotosViewModel(repo) as T
            }
        }

        setContent {
            FlickrApp2Theme {

                val vm: PhotosViewModel = viewModel(factory = factory)
                val uiState = vm.uiState.collectAsState()

                PhotosScreen(
                    uiState = uiState.value,
                    onSearch = { },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(
    uiState: UiState,
    onSearch: (String) -> Unit,
) {
    var queryString by remember { mutableStateOf(uiState.query ?: "") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(title = {Text("Flickr App")})
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = queryString,
                onValueChange = { queryString = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search") }
            )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onSearch(queryString) }
            ) {
                Text("Go")
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            PhotoGrid(
                items = uiState.items,
            )
            if (uiState.isLoading && uiState.items.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp))
            }
        }
    }
}


@Composable
fun PhotoGrid(
    items: List<FlickrPhoto>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items) { index, item ->
            PhotoItem(item)
        }
    }
}

@Composable
fun PhotoItem(photo: FlickrPhoto) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .aspectRatio(1f) // square
    ) {
        AsyncImage(
            model = photo.getImageUrl(),
            contentDescription = photo.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}