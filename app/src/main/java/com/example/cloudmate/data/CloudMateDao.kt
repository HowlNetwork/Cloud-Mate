package com.example.cloudmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cloudmate.module.CurrentWeatherObject
import com.example.cloudmate.module.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface CloudMateDao {
    @Query("SELECT * from favourite_tbl")
    fun getFavourites(): Flow<List<Favourite>>

    @Query("SELECT * from favourite_tbl where city =:city")
    suspend fun getFavouriteByCity(city: String): Favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavourite(favourite: Favourite)

    @Query("DELETE from favourite_tbl")
    suspend fun deleteAllFavourites()

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    // Current Weather Table
    @Query("SELECT * from current_tbl")
    fun getWeatherObjects(): Flow<List<CurrentWeatherObject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject)

    @Query("SELECT * from current_tbl where id =:id")
    suspend fun getWeatherById(id: Int): CurrentWeatherObject
}