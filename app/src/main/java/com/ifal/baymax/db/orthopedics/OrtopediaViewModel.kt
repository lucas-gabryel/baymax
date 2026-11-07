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
import kotlin.collections.filter

class OrtopediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrtopediaRepository
    private val apiService = OrtopediaApiService.create()

    val triageHistory: StateFlow<List<OrtopediaTriage>>

    var uiState by mutableStateOf(OrtopediaUiState())
        private set

    var userLat by mutableStateOf(-9.6658)
        private set

    var userLng by mutableStateOf(-35.7350)
        private set

    var hospitals by mutableStateOf<List<HospitalElement>>(emptyList())
        private set

    var isLoadingApi by mutableStateOf(false)
        private set

    var apiError by mutableStateOf<String?>(null)
        private set

    init {
        val dao = OrtopediaDatabase.getDatabase(application).ortopediaDao()
        repository = OrtopediaRepository(dao)
        triageHistory = repository.allTriages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

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
        val affectedBoneText = if (uiState.selectedSymptoms.isEmpty()) {
            "Nenhum sintoma selecionado"
        } else {
            uiState.selectedSymptoms.joinToString()
        }
        viewModelScope.launch {
            repository.insert(
                OrtopediaTriage(
                    painLevel = uiState.painLevel,
                    affectedBone = affectedBoneText,
                    details = uiState.details
                )
            )
        }
    }

    fun fetchNearbyHospitals(lat: Double, lng: Double) {
        userLat = lat
        userLng = lng
        viewModelScope.launch {
            isLoadingApi = true
            apiError = null
            try {
                val query = """[out:json];nwr["amenity"="hospital"](around:10000,$lat,$lng);out center;"""
                val response = apiService.getNearbyHospitals(query)

                hospitals = response.elements
                    .filter { !it.tags?.name.isNullOrBlank() && it.latitude != null && it.longitude != null }
                    .sortedBy { hospital ->
                        val results = FloatArray(1)
                        Location.distanceBetween(
                            lat, lng,
                            hospital.latitude!!, hospital.longitude!!,
                            results
                        )
                        results[0]
                    }
            } catch (e: Exception) {
                apiError = "Erro ao buscar hospitais próximos: ${e.message}"
            } finally {
                isLoadingApi = false
            }
        }
    }
}