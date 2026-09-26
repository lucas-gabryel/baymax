package com.ifal.baymax.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ifal.baymax.ui.recommendation.RecommendationScreen
import com.ifal.baymax.ui.triage.TriageScreen
import com.ifal.baymax.ui.triage.TriageViewModel

object Routes {
    const val TRIAGE = "triage"
    const val RECOMMENDATION = "recommendation"
}

@Composable
fun BaymaxNavHost(
    viewModel: TriageViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TRIAGE,
        modifier = modifier
    ) {
        composable(Routes.TRIAGE) {
            TriageScreen(
                viewModel = viewModel,
                onAnalyze = {
                    viewModel.analyze()
                    navController.navigate(Routes.RECOMMENDATION)
                }
            )
        }
        composable(Routes.RECOMMENDATION) {
            RecommendationScreen(
                viewModel = viewModel,
                onNewTriage = {
                    viewModel.startNewTriage()
                    navController.popBackStack()
                }
            )
        }
    }
}
