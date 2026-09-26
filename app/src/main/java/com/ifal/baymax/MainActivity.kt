package com.ifal.baymax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ifal.baymax.ui.components.BottomSpecialtyNav
import com.ifal.baymax.ui.navigation.BaymaxNavHost
import com.ifal.baymax.ui.navigation.Routes
import com.ifal.baymax.ui.theme.AppBackground
import com.ifal.baymax.ui.triage.TriageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
                    BaymaxApp()
                }
            }
        }
    }
}

@Composable
private fun BaymaxApp() {
    val navController = rememberNavController()
    val viewModel: TriageViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomSpecialtyNav(
                current = viewModel.currentSpecialty,
                onSelect = { specialty ->
                    viewModel.selectSpecialty(specialty)
                    if (backStackEntry?.destination?.route != Routes.TRIAGE) {
                        navController.popBackStack(Routes.TRIAGE, inclusive = false)
                    }
                }
            )
        }
    ) { innerPadding ->
        BaymaxNavHost(
            viewModel = viewModel,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
