package com.example.flickrapp2.data.model

data class PhotosPage(
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int,
    val photo: List<FlickrPhoto>
)
