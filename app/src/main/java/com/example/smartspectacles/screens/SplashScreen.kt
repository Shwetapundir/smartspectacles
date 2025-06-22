package com.example.smartspectacles.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.smartspectacles.datasource.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
@Composable
fun SplashScreen(navController: NavHostController, userPreferences: UserPreferences) {
    val userState = userPreferences.userFlow.collectAsState(initial = null)

    LaunchedEffect(true) {
        delay(2000)
        val user = userState.value
        if (user != null) {
            if (user.token.isNotEmpty()) {
                navController.navigate("landingscreen") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}


