package com.ifal.baymax.ui.triage

enum class Outcome { NORMAL, EMERGENCY, CRISIS }

fun determineOutcome(specialty: Specialty, painLevel: Int): Outcome {
    val isUrgent = painLevel >= specialty.emergencyThreshold
    return when {
        isUrgent && specialty.mode == TriageMode.CRISIS -> Outcome.CRISIS
        isUrgent -> Outcome.EMERGENCY
        else -> Outcome.NORMAL
    }
}

fun buildSymptomsSummary(selectedChips: Set<String>, notes: String): String {
    val trimmedNotes = notes.trim()
    val items = selectedChips + listOfNotNull(trimmedNotes.takeIf { it.isNotEmpty() })
    return items.joinToString(", ").ifEmpty { "nenhum sintoma detalhado" }
}
