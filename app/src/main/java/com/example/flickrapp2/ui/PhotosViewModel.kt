package com.example.flickrapp2.ui

import androidx.lifecycle.ViewModel
import com.example.flickrapp2.data.remote.FlickrRepository

class PhotosViewModel(private val repo: FlickrRepository) : ViewModel() {
    init { }
}
