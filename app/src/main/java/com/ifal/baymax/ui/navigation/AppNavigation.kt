package com.ifal.baymax.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaResultScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaScreen
import com.ifal.baymax.db.orthopedics.OrtopediaViewModel
import com.ifal.baymax.ui.screens.cardiology.CardiologiaScreen
import com.ifal.baymax.ui.screens.geral.GeralHistoricoScreen
import com.ifal.baymax.ui.screens.geral.GeralResultadoScreen
import com.ifal.baymax.ui.screens.geral.GeralScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaApiScreen

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Geral.route, "Geral", "\uD83E\uDE7A"),
    BottomNavItem(Screen.Ortopedia.route, "Ortopedia", "\uD83E\uDDB4"),
    BottomNavItem(Screen.Cardiologia.route, "Cardio", "\uD83D\uDC93"),
    BottomNavItem(Screen.Psicologia.route, "Mental", "\uD83E\uDDE0")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val ortopediaViewModel: OrtopediaViewModel = viewModel()


    Scaffold(
        bottomBar = {
            MainBottomNavigation(
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Geral.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Geral.route) {
                GeralScreen(navController = navController)
            }

            composable(
                Screen.GeralResultado.route,
                arguments = listOf(navArgument("nivelDor") { type = NavType.IntType })
            ) { backStackEntry ->
                GeralResultadoScreen(
                    nivelDor = backStackEntry.arguments?.getInt("nivelDor") ?: 0,
                    onNovaTriagem = { navController.popBackStack() },
                    onVerHistorico = { navController.navigate(Screen.GeralHistorico.route) }
                )
            }

            composable(Screen.GeralHistorico.route) {
                GeralHistoricoScreen()
            }

            composable(Screen.Ortopedia.route) {
                OrtopediaScreen(
                    uiState = ortopediaViewModel.uiState,
                    onPainLevelChange = ortopediaViewModel::updatePainLevel,
                    onToggleSymptom = ortopediaViewModel::toggleSymptom,
                    onDetailsChange = ortopediaViewModel::updateDetails,
                    onEvaluate = {
                        ortopediaViewModel.analyze()
                        navController.navigate(Screen.OrtopediaResultado.route)
                    }
                )
            }

            composable(Screen.Cardiologia.route) {
                CardiologiaScreen(navController = navController)
            }
            composable(Screen.CardiologiaResultado.route) {
                com.ifal.baymax.ui.screens.cardiology.CardiologiaResultScreen()
            }

            composable(Screen.Psicologia.route) {
                Text("Saúde Mental")
            }

            composable(Screen.OrtopediaResultado.route) {
                val history by ortopediaViewModel.triageHistory.collectAsState()

                OrtopediaResultScreen(
                    painLevel = ortopediaViewModel.uiState.painLevel,
                    selectedSymptoms = ortopediaViewModel.uiState.selectedSymptoms,
                    details = ortopediaViewModel.uiState.details,
                    history = history,
                    onNewTriage = {
                        ortopediaViewModel.reset()
                        navController.popBackStack()
                    },
                    onViewRoute = {
                        navController.navigate(Screen.OrtopediaEspecialistas.route)
                    }
                )
            }
            composable(Screen.OrtopediaEspecialistas.route) {
                OrtopediaApiScreen(
                    hospitals = ortopediaViewModel.hospitals,
                    isLoading = ortopediaViewModel.isLoadingApi,
                    errorMessage = ortopediaViewModel.apiError,
                    onLoadHospitals = ortopediaViewModel::fetchNearbyHospitals,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun MainBottomNavigation(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val selectedRoute = when {
                currentRoute == Screen.OrtopediaResultado.route -> Screen.Ortopedia.route
                currentRoute?.startsWith("geral") == true -> Screen.Geral.route
                else -> currentRoute
            }

            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Text(item.icon)
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}