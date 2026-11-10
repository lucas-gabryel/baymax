package com.ifal.baymax.db.orthopedics

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ifal.baymax.api.orthopedics.HospitalElement
import com.ifal.baymax.api.orthopedics.OrtopediaApiService
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrtopediaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = OrtopediaDatabase.getDatabase(application).ortopediaDao()
    private val apiService = OrtopediaApiService.create()
    val triageHistory: StateFlow<List<OrtopediaTriage>> = dao.getAllTriages().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    var uiState by mutableStateOf(OrtopediaUiState())
        private set
    var hospitals by mutableStateOf<List<HospitalElement>>(emptyList())
        private set
    var isLoadingApi by mutableStateOf(false)
        private set
    var apiError by mutableStateOf<String?>(null)
        private set
    fun updatePainLevel(painLevel: Int) {
        uiState = uiState.copy(painLevel = painLevel.coerceIn(0, 10))
    }
    fun toggleSymptom(symptom: String) {
        val updated = if (symptom in uiState.selectedSymptoms) {
            uiState.selectedSymptoms - symptom
        } else {
            uiState.selectedSymptoms + symptom
        }
        uiState = uiState.copy(selectedSymptoms = updated)
    }
    fun updateDetails(details: String) {
        uiState = uiState.copy(details = details)
    }
    fun reset() {
        uiState = OrtopediaUiState()
    }
    fun analyze() {
        val affectedBone = uiState.selectedSymptoms.ifEmpty { setOf("Nenhum sintoma selecionado") }.joinToString()
        viewModelScope.launch {
            dao.insert(
                OrtopediaTriage(
                    painLevel = uiState.painLevel,
                    affectedBone = affectedBone,
                    details = uiState.details
                )
            )
        }
    }
    fun setLocationError(message: String) {
        isLoadingApi = false
        hospitals = emptyList()
        apiError = message
    }
    fun fetchNearbyHospitals(lat: Double, lng: Double) {
        viewModelScope.launch {
            isLoadingApi = true
            apiError = null
            try {
                val viewbox = "${lng - 0.1},${lat + 0.1},${lng + 0.1},${lat - 0.1}"
                hospitals = apiService.getNearbyHospitals(viewbox)
            } catch (e: Exception) {
                apiError = "Erro ao buscar hospitais: ${e.message}"
            } finally {
                isLoadingApi = false
            }
        }
    }
}