package com.example.cloudmate.data

import com.example.cloudmate.module.CurrentWeatherObject
import com.example.cloudmate.module.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val cloudMateDao: CloudMateDao) {
    fun getFavourites(): Flow<List<Favourite>> = cloudMateDao.getFavourites()
    suspend fun insertFavourite(favourite: Favourite) = cloudMateDao.insertFavourite(favourite)
    suspend fun updateFavourite(favourite: Favourite) = cloudMateDao.updateFavourite(favourite)
    suspend fun deleteAllFavourite() = cloudMateDao.deleteAllFavourites()
    suspend fun deleteFavourite(favourite: Favourite) = cloudMateDao.deleteFavourite(favourite)
    suspend fun getFavByCity(city: String): Favourite = cloudMateDao.getFavouriteByCity(city)

    // Current weather table
    fun getWeatherObjects(): Flow<List<CurrentWeatherObject>> = cloudMateDao.getWeatherObjects()
    suspend fun getWeatherById(id: Int): CurrentWeatherObject = cloudMateDao.getWeatherById(id)
    suspend fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = cloudMateDao.insertCurrentWeatherObject(currentWeatherObject)
    suspend fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = cloudMateDao.updateCurrentWeatherObject(currentWeatherObject)
}