package com.ifal.baymax.ui.triage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class TriageMode { EMERGENCY, CRISIS }

data class Specialty(
    val key: String,
    val title: String,
    val color: Color,
    val colorDark: Color,
    val colorSoft: Color,
    val icon: ImageVector,
    val question: String,
    val chips: List<String>,
    val emergencyThreshold: Int,
    val mode: TriageMode
)

val Specialties: List<Specialty> = listOf(
    Specialty(
        key = "geral",
        title = "Clínica Geral",
        color = Color(0xFFEF4444),
        colorDark = Color(0xFFDC2626),
        colorSoft = Color(0xFFFEE2E2),
        icon = Icons.Filled.MedicalServices,
        question = "Qual o seu nível de dor?",
        chips = listOf("Febre", "Dor de cabeça", "Enjoo", "Dor de garganta"),
        emergencyThreshold = 8,
        mode = TriageMode.EMERGENCY
    ),
    Specialty(
        key = "ortopedia",
        title = "Ortopedia",
        color = Color(0xFFFB923C),
        colorDark = Color(0xFFEA580C),
        colorSoft = Color(0xFFFFEDD5),
        icon = Icons.Filled.Healing,
        question = "Qual o nível da dor articular?",
        chips = listOf("Tornozelo", "Coluna", "Joelho", "Pancada recente"),
        emergencyThreshold = 8,
        mode = TriageMode.EMERGENCY
    ),
    Specialty(
        key = "cardiologia",
        title = "Cardiologia",
        color = Color(0xFFE11D48),
        colorDark = Color(0xFF9F1239),
        colorSoft = Color(0xFFFFE4E6),
        icon = Icons.Filled.Favorite,
        question = "Qual o nível de desconforto no peito?",
        chips = listOf("Palpitação", "Falta de ar", "Pressão alta", "Tontura"),
        emergencyThreshold = 6,
        mode = TriageMode.EMERGENCY
    ),
    Specialty(
        key = "psicologia",
        title = "Saúde Mental",
        color = Color(0xFF6366F1),
        colorDark = Color(0xFF9333EA),
        colorSoft = Color(0xFFE0E7FF),
        icon = Icons.Filled.Psychology,
        question = "Qual o seu nível de ansiedade hoje?",
        chips = listOf("Pânico", "Tristeza", "Insônia", "Estresse"),
        emergencyThreshold = 7,
        mode = TriageMode.CRISIS
    )
)
