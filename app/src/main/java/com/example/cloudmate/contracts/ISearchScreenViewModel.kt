package com.example.cloudmate.contracts

import com.example.cloudmate.module.Favourite
import kotlinx.coroutines.flow.StateFlow

interface ISearchScreenViewModel {
    val favList: StateFlow<List<Favourite>>
    fun insertFavourite(favourite: Favourite)
    fun updateFavourite(favourite: Favourite)
    fun deleteFavourite(favourite: Favourite)
}