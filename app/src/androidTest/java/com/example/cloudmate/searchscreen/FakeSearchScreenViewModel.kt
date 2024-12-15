package com.example.cloudmate.searchscreen


import androidx.lifecycle.ViewModel
import com.example.cloudmate.contracts.ISearchScreenViewModel
import com.example.cloudmate.module.Favourite
import com.example.cloudmate.screens.search.SearchScreenViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSearchScreenViewModel : ISearchScreenViewModel {

    // Tạo StateFlow giả để mô phỏng danh sách các yêu thích
    private val _favList = MutableStateFlow<List<Favourite>>(emptyList())
    override val favList: StateFlow<List<Favourite>> = _favList.asStateFlow()

    // Hàm để thêm một thành phố yêu thích
    override fun insertFavourite(favourite: Favourite) {
        val updatedList = _favList.value.toMutableList()
        updatedList.add(favourite)
        _favList.value = updatedList
    }

    // Hàm để cập nhật một thành phố yêu thích
    override fun updateFavourite(favourite: Favourite) {
        val updatedList = _favList.value.map {
            if (it.city == favourite.city) favourite else it
        }
        _favList.value = updatedList
    }

    // Hàm để xóa một thành phố yêu thích
    override fun deleteFavourite(favourite: Favourite) {
        val updatedList = _favList.value.filter { it.city != favourite.city }
        _favList.value = updatedList
    }
}
