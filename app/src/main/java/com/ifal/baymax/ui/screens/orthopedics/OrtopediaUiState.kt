package com.ifal.baymax.ui.screens.orthopedics

data class OrtopediaUiState(
    val painLevel: Int = 5,
    val selectedSymptoms: Set<String> = emptySet(),
    val details: String = "",
    val resultVisible: Boolean = false
)
