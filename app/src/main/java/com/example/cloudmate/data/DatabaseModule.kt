package com.example.cloudmate.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class DatabaseModule {
    @Singleton
    @Provides
    fun provideWeatherDao(cloudMateDatabase: CloudMateDatabase): CloudMateDao =
        cloudMateDatabase.cloudMateDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): CloudMateDatabase =
        Room.databaseBuilder(
            context,
            CloudMateDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration().build()
}