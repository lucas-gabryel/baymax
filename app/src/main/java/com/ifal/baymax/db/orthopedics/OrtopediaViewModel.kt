package com.ifal.baymax.db.orthopedics

import android.app.Application
import android.location.Location
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
    private val repository = OrtopediaRepository(dao)
    private val apiService = OrtopediaApiService.create()
    val triageHistory: StateFlow<List<OrtopediaTriage>> = repository.allTriages.stateIn(
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
        uiState = uiState.copy(resultVisible = true)
        val affectedBone = uiState.selectedSymptoms.ifEmpty { setOf("Nenhum sintoma selecionado") }.joinToString()
        viewModelScope.launch {
            repository.insert(
                OrtopediaTriage(
                    painLevel = uiState.painLevel,
                    affectedBone = affectedBone,
                    details = uiState.details
                )
            )
        }
    }
    fun fetchNearbyHospitals(lat: Double = -9.6658, lng: Double = -35.7350) {
        viewModelScope.launch {
            isLoadingApi = true
            apiError = null
            try {
                val viewbox = "${lng - 0.1},${lat + 0.1},${lng + 0.1},${lat - 0.1}"
                hospitals = apiService.getNearbyHospitals(viewbox).sortedBy { hospital ->
                    val dist = FloatArray(1)
                    Location.distanceBetween(lat, lng, hospital.lat, hospital.lon, dist)
                    dist[0]
                }
            } catch (e: Exception) {
                apiError = "Erro ao buscar hospitais: ${e.message}"
            } finally {
                isLoadingApi = false
            }
        }
    }
}