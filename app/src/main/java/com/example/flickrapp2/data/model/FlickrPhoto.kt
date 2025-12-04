package com.example.flickrapp2.data.model

data class FlickrPhoto(
    val id: String,
    val owner: String,
    val secret: String,
    val serverId: String,
    val farm: Int?,
    val title: String
)

/*
Documentation: https://www.flickr.com/services/api/misc.urls.html
Format: https://live.staticflickr.com/{server-id}/{id}_{secret}.jpg
 */
fun FlickrPhoto.imageUrl(): String {
    return "https://live.staticflickr.com/${serverId}/${id}_${secret}.jpg"
}