package com.example.flickrapp2.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlickrRepository(private val api: FlickrApi, private val apiKey: String) {

    suspend fun searchPhotos(query: String, page: Int, perPage: Int = 30) = withContext(Dispatchers.IO) {
        api.searchPhotos(apiKey = apiKey, text = query, page = page, perPage = perPage)
    }

    suspend fun recentPhotos(page: Int, perPage: Int = 30) = withContext(Dispatchers.IO) {
        api.recentPhotos(apiKey = apiKey, page = page, perPage = perPage)
    }
}
