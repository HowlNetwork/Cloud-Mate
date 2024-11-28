package com.example.cloudmate

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cloudmate.data.CloudMateDao
import com.example.cloudmate.data.CloudMateDatabase
import com.example.cloudmate.module.CurrentWeatherObject
import com.example.cloudmate.module.Favourite
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CloudMateDaoTest {

    private lateinit var database: CloudMateDatabase
    private lateinit var dao: CloudMateDao

    @Before
    fun setUp() {
        // Sử dụng in-memory database để test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CloudMateDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.cloudMateDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeatherDataAndRetrieve() = runBlocking {
        val testWeather = CurrentWeatherObject(
            id = 1,
            weather = "Cloud"
        )

        dao.insertCurrentWeatherObject(testWeather)
        val retrievedWeather = dao.getWeatherById(1)

        assertNotNull(retrievedWeather)

        assertEquals(testWeather.weather, retrievedWeather?.weather)
    }

    @Test
    fun deleteWeatherFavourite() = runBlocking {
        val testWeatherFavourite = Favourite(
            city = "Hanoi",
            lat = 21.0277633,
            lon = 105.8341583
        )

        dao.insertFavourite(testWeatherFavourite)
        dao.deleteFavourite(testWeatherFavourite)

        val retrievedWeather = dao.getFavouriteByCity("Hanoi")
        assertNull(retrievedWeather)
    }

    @Test
    fun updateWeatherFavourite() = runBlocking {
        val testWeather = Favourite(
            city = "Hanoi",
            lat = 21.0277633,
            lon = 105.8341583
        )

        dao.insertFavourite(testWeather)

        val updatedWeather = testWeather.copy(city = "Hanoi",   lat = 21.0277633,
            lon = 105.8341583 )
        dao.updateFavourite(updatedWeather)

        val retrievedWeather = dao.getFavouriteByCity("Hanoi")

        assertNotNull(retrievedWeather)
        assertEquals( 21.0277633 , retrievedWeather?.lat)
        assertEquals(105.8341583, retrievedWeather?.lon)
    }
}