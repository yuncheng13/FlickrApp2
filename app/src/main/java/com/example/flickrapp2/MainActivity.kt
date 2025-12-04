package com.example.flickrapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flickrapp2.data.remote.FlickrRepository
import com.example.flickrapp2.data.remote.RetrofitClient
import com.example.flickrapp2.ui.PhotosViewModel
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

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlickrApp2Theme {
        Greeting("Android")
    }
}