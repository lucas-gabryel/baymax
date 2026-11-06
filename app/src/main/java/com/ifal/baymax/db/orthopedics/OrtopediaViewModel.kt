package com.ifal.baymax.db.orthopedics

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ifal.baymax.api.orthopedics.OrtopediaApiService
import com.ifal.baymax.api.orthopedics.OrtopediaSpecialist
import com.ifal.baymax.ui.screens.orthopedics.OrtopediaUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrtopediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OrtopediaRepository
    private val apiService = OrtopediaApiService.create()

    val triageHistory: StateFlow<List<OrtopediaTriage>>

    var uiState by mutableStateOf(OrtopediaUiState())
        private set

    var specialists by mutableStateOf<List<OrtopediaSpecialist>> (emptyList())
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

    fun fetchSpecialists() {
        viewModelScope.launch {
            isLoadingApi = true
            apiError = null
            try {
                specialists = apiService.getSpecialists()
            } catch (e: Exception) {
                apiError = "Erro ao carregar especialistas: ${e.message}"
            } finally {
                isLoadingApi = false
            }
        }
    }
}