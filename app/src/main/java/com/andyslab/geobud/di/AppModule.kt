package com.andyslab.geobud.di

import android.content.Context
import com.andyslab.geobud.data.remote.LandmarkPhotoAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context{
        return context
    }

    @Provides
    @Singleton
    fun provideApi(): LandmarkPhotoAPI{
        return Retrofit.Builder()
                .baseUrl("https://api.pexels.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LandmarkPhotoAPI::class.java)
    }
}