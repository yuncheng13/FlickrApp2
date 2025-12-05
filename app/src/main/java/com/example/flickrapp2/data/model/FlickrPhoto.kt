package com.example.flickrapp2.data.model

data class FlickrPhoto(
    val id: String,
    val secret: String,
    val server: String,
    val title: String
)

/*
Documentation: https://www.flickr.com/services/api/misc.urls.html
Format: https://live.staticflickr.com/{server}/{id}_{secret}.jpg
 */
fun FlickrPhoto.getImageUrl(): String {
    return "https://live.staticflickr.com/${server}/${id}_${secret}.jpg"
}