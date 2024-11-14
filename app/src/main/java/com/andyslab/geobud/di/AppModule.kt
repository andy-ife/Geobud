package com.andyslab.geobud.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.andyslab.geobud.data.local.db.LandmarkDatabase
import com.andyslab.geobud.data.local.sources.CountriesDataSource
import com.andyslab.geobud.data.remote.LandmarkPhotoAPI
import com.andyslab.geobud.data.repository.landmark.LandmarkRepoImpl
import com.andyslab.geobud.data.repository.landmark.LandmarkRepository
import com.andyslab.geobud.data.repository.player.PlayerRepoImpl
import com.andyslab.geobud.data.repository.player.PlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "player_data")

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context{
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LandmarkDatabase{
        return Room.databaseBuilder(context, LandmarkDatabase::class.java, "landmark.db")
            .createFromAsset("database/landmark.db")
            .build()
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

    @Provides
    @Singleton
    fun provideCountriesDataSource(): CountriesDataSource {
        return CountriesDataSource()
    }

    @Provides
    @Singleton
    fun providePlayerDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun providePlayerRepository(
        countries: CountriesDataSource,
        db: LandmarkDatabase,
        dataStore: DataStore<Preferences>
    ): PlayerRepository{
        return PlayerRepoImpl(dataStore, countries, db)
    }

    @Provides
    @Singleton
    fun provideLandmarkRepository(
        @ApplicationContext context: Context,
        db: LandmarkDatabase,
        api: LandmarkPhotoAPI,
        repo: PlayerRepository
    ): LandmarkRepository{
        return LandmarkRepoImpl(context, db, api, repo)
    }
}