package com.andyslab.geobud.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: LandmarkPhotoAPI by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.pexels.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LandmarkPhotoAPI::class.java)
    }
}