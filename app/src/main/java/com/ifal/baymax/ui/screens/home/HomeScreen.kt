package com.ifal.baymax.ui.screens.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ifal.baymax.ui.navigation.ORTOPEDIA_ROUTE

@Composable
fun HomeScreen(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(ORTOPEDIA_ROUTE)
        }
    ) {
        Text("Abrir Ortopedia")
    }
}