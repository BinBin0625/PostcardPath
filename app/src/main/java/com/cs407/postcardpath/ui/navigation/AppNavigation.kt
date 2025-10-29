package com.cs407.postcardpath.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs407.postcardpath.ui.screens.LandingScreen
import com.cs407.postcardpath.ui.screens.MainPage
import com.cs407.postcardpath.ui.screens.TakePhoto

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {
        composable("landing") { LandingScreen(navController = navController) }
        composable("main") { MainPage() }
    }
}
