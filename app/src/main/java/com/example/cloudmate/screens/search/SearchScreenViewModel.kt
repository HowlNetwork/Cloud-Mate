package com.example.cloudmate.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudmate.data.WeatherDbRepository
import com.example.cloudmate.module.Favourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchScreenRepository: WeatherDbRepository) : ViewModel()
{
    private val _favList = MutableStateFlow<List<Favourite>>(emptyList())
    val favList = _favList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            searchScreenRepository.getFavourites().distinctUntilChanged()
                .collect { listOfFavs ->
                    if (listOfFavs.isNullOrEmpty()) {
                        Log.d("TAG", "Empty favs")
                    } else {
                        _favList.value = listOfFavs
                        Log.d("TAG", "${favList.value}")
                    }

                }

        }
    }

    fun insertFavourite(favourite: Favourite) = viewModelScope.launch { searchScreenRepository.insertFavourite(favourite) }
    fun updateFavourite(favourite: Favourite) = viewModelScope.launch { searchScreenRepository.updateFavourite(favourite) }
    fun deleteFavourite(favourite: Favourite) = viewModelScope.launch { searchScreenRepository.deleteFavourite(favourite) }

}