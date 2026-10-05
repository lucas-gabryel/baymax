package com.ifal.baymax.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaResultScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaScreen
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaViewModel

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
            startDestination = Screen.Ortopedia.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Geral.route) {
                Text("Geral")
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
                Text("Cardiologia")
            }

            composable(Screen.Psicologia.route) {
                Text("Saúde Mental")
            }

            composable(Screen.OrtopediaResultado.route) {
                OrtopediaResultScreen(
                    painLevel = ortopediaViewModel.uiState.painLevel,
                    selectedSymptoms = ortopediaViewModel.uiState.selectedSymptoms,
                    details = ortopediaViewModel.uiState.details,
                    onNewTriage = {
                        ortopediaViewModel.reset()
                        navController.popBackStack()
                    },
                    onViewRoute = {
                        // Mock temporário.
                    }
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
            val selectedRoute = if (currentRoute == Screen.OrtopediaResultado.route) {
                Screen.Ortopedia.route
            } else {
                currentRoute
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