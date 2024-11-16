package com.example.cloudmate.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDate(): String {
    // Get Current Date time in localized style
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEE, d MMM y", Locale.getDefault())
    return current.format(formatter)
}