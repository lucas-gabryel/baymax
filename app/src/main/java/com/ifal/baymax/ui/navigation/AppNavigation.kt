package com.ifal.baymax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.ifal.baymax.ui.screens.home.HomeScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaScreen

const val HOME_ROUTE = "inicio"
const val ORTOPEDIA_ROUTE = "ortopedia"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        composable(HOME_ROUTE) {
            HomeScreen(navController)
        }

        composable(ORTOPEDIA_ROUTE) {
            OrtopediaScreen()
        }
    }
}