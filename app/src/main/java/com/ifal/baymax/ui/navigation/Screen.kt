package com.ifal.baymax.ui.navigation

sealed class Screen(val route: String) {
    data object Geral : Screen("geral")
    data object Ortopedia : Screen("ortopedia")
    data object Cardiologia : Screen("cardio")
    data object Psicologia : Screen("mental")
    data object OrtopediaResultado : Screen("ortopedia_resultado")
}