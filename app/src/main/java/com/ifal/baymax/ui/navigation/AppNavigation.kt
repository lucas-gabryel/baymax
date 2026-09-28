package com.ifal.baymax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.ifal.baymax.ui.screens.home.HomeScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaResultScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaViewModel

const val HOME_ROUTE = "inicio"
const val ORTOPEDIA_ROUTE = "ortopedia"
const val ORTOPEDIA_RESULT_ROUTE = "ortopedia_resultado"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val ortopediaViewModel: OrtopediaViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = ORTOPEDIA_ROUTE
    ) {
        composable(HOME_ROUTE) {
            HomeScreen(navController)
        }

        composable(ORTOPEDIA_ROUTE) {
            OrtopediaScreen(
                uiState = ortopediaViewModel.uiState,
                onPainLevelChange = ortopediaViewModel::updatePainLevel,
                onToggleSymptom = ortopediaViewModel::toggleSymptom,
                onDetailsChange = ortopediaViewModel::updateDetails,
                onEvaluate = {
                    ortopediaViewModel.analyze()
                    navController.navigate(ORTOPEDIA_RESULT_ROUTE)
                }
            )
        }

        composable(ORTOPEDIA_RESULT_ROUTE) {
            OrtopediaResultScreen(
                painLevel = ortopediaViewModel.uiState.painLevel,
                selectedSymptoms = ortopediaViewModel.uiState.selectedSymptoms,
                details = ortopediaViewModel.uiState.details,
                onNewTriage = {
                    ortopediaViewModel.reset()
                    navController.popBackStack()
                }
            )
        }
    }
}