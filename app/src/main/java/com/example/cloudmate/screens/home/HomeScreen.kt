package com.example.cloudmate.screens.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    city: String?,
    context: Context
) {
    Log.d("City", "$city")
    lateinit var latitude: MutableState<Double>
    lateinit var longitude: MutableState<Double>

    if (city == "default") {
        latitude = remember {
            mutableStateOf(360.0)
        }
        longitude = remember {
            mutableStateOf(360.0)
        }
    } else {
        val address = city?.let { getLatLon(context, it) }
        if (address != null) {
            latitude = remember {
                mutableStateOf(address.latitude)
            }
            longitude = remember {
                mutableStateOf(address.longitude)
            }
        } else {
            latitude = remember {
                mutableStateOf(360.0)
            }
            longitude = remember {
                mutableStateOf(360.0)
            }
            Toast.makeText(context, "Unknown Location", Toast.LENGTH_LONG).show()
            /*Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.unknown_search),
                    fontFamily = poppinsFamily,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    onClick = {
                        navController.navigate(BottomNavItem.Search.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFFd68118)),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(
                        stringResource(R.string.try_again),
                        fontFamily = poppinsFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }
            }*/
        }
    }
    val gradientColors = listOf(Color(0xFF060620), MaterialTheme.colorScheme.primary)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                HomeScreenElements(
                    context = context,
                    latitude = latitude,
                    longitude = longitude,
                )
            }
        },
         containerColor = Color.Transparent)
    }
}
