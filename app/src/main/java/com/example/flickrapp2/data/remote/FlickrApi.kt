package com.example.flickrapp2.data.remote

import com.example.flickrapp2.data.model.FlickrResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    // Example query:
    //https://www.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=a0222db49599
    //9c951dc33702500fdc4d&format=json&nojsoncallback=1
    @GET("services/rest/")
    suspend fun recentPhotos(
        @Query("method") method: String = "flickr.photos.getRecent",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1
    ): FlickrResponse

    // Example query:
    //https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=a0222db495999c
    //951dc33702500fdc4d&text=spiderman&format=json&nojsoncallback=1
    @GET("services/rest/")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String,
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 30,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1
    ): FlickrResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.flickr.com/"

    val api: FlickrApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickrApi::class.java)
    }
}