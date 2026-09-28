package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OrtopediaViewModel : ViewModel() {
    var uiState by mutableStateOf(OrtopediaUiState())
        private set

    fun updatePainLevel(painLevel: Int) {
        uiState = uiState.copy(painLevel = painLevel.coerceIn(0, 10))
    }

    fun toggleSymptom(symptom: String) {
        val updatedSymptoms = if (symptom in uiState.selectedSymptoms) {
            uiState.selectedSymptoms - symptom
        } else {
            uiState.selectedSymptoms + symptom
        }

        uiState = uiState.copy(selectedSymptoms = updatedSymptoms)
    }

    fun updateDetails(details: String) {
        uiState = uiState.copy(details = details)
    }

    fun reset() {
        uiState = OrtopediaUiState()
    }

    fun analyze() {
        uiState = uiState.copy(resultVisible = true)
    }
}
