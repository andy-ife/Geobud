package com.andyslab.geobud.data.remote

import androidx.annotation.Keep
import com.andyslab.geobud.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

@Keep
interface LandmarkPhotoAPI {
    @Headers("Authorization: ${BuildConfig.PEXELS_API_KEY}")
    @GET("/v1/search")
    suspend fun getLandmarkPhoto(
        @Query("query")searchTerm: String,
        @Query("page")page: Int,
        @Query("per_page")perPage: Int,
    ): Response<LandmarkPhotoDto>
}
