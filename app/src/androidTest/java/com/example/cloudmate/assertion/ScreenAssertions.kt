package com.example.cloudmate.assertion

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    val actualRoute = currentBackStackEntry?.destination?.route?.split("/")?.firstOrNull()
    assertEquals(expectedRouteName, actualRoute)
}