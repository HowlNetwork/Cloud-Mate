package com.example.cloudmate.screens.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudmate.worker.WorkManagerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val workManagerRepository = WorkManagerRepository(application)

    // StateFlow để theo dõi trạng thái Switch
    private val _isNotificationEnabled = MutableStateFlow(false)
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled

    fun enableNotifications() {
        _isNotificationEnabled.value = true
        workManagerRepository.scheduleWeatherAlert()
    }

    fun disableNotifications() {
        _isNotificationEnabled.value = false
        workManagerRepository.cancelWeatherAlert()
    }

    // Cập nhật trạng thái Switch theo quyền
    fun updateNotificationState(isEnabled: Boolean) {
        viewModelScope.launch {
            _isNotificationEnabled.value = isEnabled
        }
    }
}
