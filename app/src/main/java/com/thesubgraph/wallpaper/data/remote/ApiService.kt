package com.thesubgraph.wallpaper.data.remote

import android.util.Size
import com.thesubgraph.wallpaper.data.common.WebServiceInterface
import com.thesubgraph.wallpaper.data.serialization.PhotoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService : WebServiceInterface {
    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perSize: Int
    ): Response<List<PhotoDto>>

}
