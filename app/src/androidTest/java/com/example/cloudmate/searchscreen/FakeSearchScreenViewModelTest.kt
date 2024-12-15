package com.example.cloudmate.searchscreen

import com.example.cloudmate.module.Favourite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakeSearchScreenViewModelTest {
    private lateinit var viewModel: FakeSearchScreenViewModel

    @Before
    fun setup() {
        viewModel = FakeSearchScreenViewModel()
    }

    @Test
    fun testInsertFavourite() = runTest {
        val favourite = Favourite(city = "Hanoi", lat = 21.0285, lon = 105.8542)
        viewModel.insertFavourite(favourite)

        val favList = viewModel.favList.first()
        assertEquals(1, favList.size)
        assertEquals(favourite, favList[0])
    }

    @Test
    fun testUpdateFavourite() = runTest {
        val favourite = Favourite(city = "Hanoi", lat = 21.0285, lon = 105.8542)
        viewModel.insertFavourite(favourite)

        val updatedFavourite = Favourite(city = "Hanoi", lat = 21.033, lon = 105.85)
        viewModel.updateFavourite(updatedFavourite)

        val favList = viewModel.favList.first()
        assertEquals(1, favList.size)
        assertEquals(updatedFavourite, favList[0])
    }

    @Test
    fun testDeleteFavourite() = runTest {
        val favourite1 = Favourite(city = "Hanoi", lat = 21.0285, lon = 105.8542)
        val favourite2 = Favourite(city = "HoChiMinh", lat = 10.8231, lon = 106.6297)

        viewModel.insertFavourite(favourite1)
        viewModel.insertFavourite(favourite2)

        viewModel.deleteFavourite(favourite1)

        val favList = viewModel.favList.first()
        assertEquals(1, favList.size)
        assertEquals(favourite2, favList[0])
    }

    @Test
    fun testDeleteFavourite_NotInList() = runTest {
        val favourite = Favourite(city = "Hanoi", lat = 21.0285, lon = 105.8542)
        viewModel.deleteFavourite(favourite) // Attempt to delete a city that isn't in the list

        val favList = viewModel.favList.first()
        assertEquals(0, favList.size) // List should remain empty
    }

    @Test
    fun testInsertDuplicateFavourite() = runTest {
        val favourite = Favourite(city = "Hanoi", lat = 21.0285, lon = 105.8542)
        viewModel.insertFavourite(favourite)
        viewModel.insertFavourite(favourite) // Attempt to insert duplicate

        val favList = viewModel.favList.first()
        assertEquals(2, favList.size) // Ensure duplicates are handled properly
    }
}