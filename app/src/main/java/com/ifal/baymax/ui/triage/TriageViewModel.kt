package com.ifal.baymax.ui.triage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class TriageResult(
    val specialty: Specialty,
    val painLevel: Int,
    val symptomsSummary: String,
    val outcome: Outcome
)

class TriageViewModel : ViewModel() {

    var currentSpecialty by mutableStateOf(Specialties.first())
        private set

    var painLevel by mutableStateOf(5)
        private set

    var selectedChips by mutableStateOf(emptySet<String>())
        private set

    var notes by mutableStateOf("")
        private set

    var result by mutableStateOf<TriageResult?>(null)
        private set

    fun selectSpecialty(specialty: Specialty) {
        currentSpecialty = specialty
        resetTriageFields()
    }

    fun updatePainLevel(value: Int) {
        painLevel = value
    }

    fun toggleChip(chip: String) {
        selectedChips = if (chip in selectedChips) selectedChips - chip else selectedChips + chip
    }

    fun updateNotes(value: String) {
        notes = value
    }

    fun analyze() {
        result = TriageResult(
            specialty = currentSpecialty,
            painLevel = painLevel,
            symptomsSummary = buildSymptomsSummary(selectedChips, notes),
            outcome = determineOutcome(currentSpecialty, painLevel)
        )
    }

    fun startNewTriage() {
        resetTriageFields()
    }

    private fun resetTriageFields() {
        painLevel = 5
        selectedChips = emptySet()
        notes = ""
        result = null
    }
}
