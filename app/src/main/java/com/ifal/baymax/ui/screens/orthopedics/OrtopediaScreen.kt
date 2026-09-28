package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun OrtopediaScreen(
    uiState: OrtopediaUiState,
    onPainLevelChange: (Int) -> Unit,
    onToggleSymptom: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onEvaluate: () -> Unit
) {
    val symptoms = listOf(
        "Tornozelo",
        "Coluna",
        "Joelho",
        "Pancada recente"
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OrtopediaHeader()

        PainLevelCard(
            painLevel = uiState.painLevel,
            onPainLevelChange = onPainLevelChange
        )

        symptoms.forEach { symptom ->
            val isSelected = symptom in uiState.selectedSymptoms

            SymptomChip(
                text = symptom,
                selected = isSelected,
                onClick = {
                    onToggleSymptom(symptom)
                }
            )
        }

        OutlinedTextField(
            value = uiState.details,
            onValueChange = onDetailsChange,
            label = {
                Text("Outros detalhes (opcional)")
            },
            placeholder = {
                Text("Descreva algo que os sintomas acima não cobrem...")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            minLines = 3
        )

        Button(
            onClick = onEvaluate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEA580C)
            )
        ) {
            Text("Avaliar quadro")
        }
    }
}

@Composable
private fun OrtopediaHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFB923C),
                        Color(0xFFEA580C)
                    )
                )
            )
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "\uD83E\uDDB4",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Ortopedia",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Assistente de triagem",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun PainLevelCard(
    painLevel: Int,
    onPainLevelChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Qual o nível da dor articular?",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("\uD83D\uDE42")
                Text("\uD83D\uDE10")
                Text("\uD83D\uDE1F")
                Text("\uD83D\uDE2B")
                Text("\uD83D\uDE2D")
            }

            Slider(
                value = painLevel.toFloat(),
                onValueChange = { value ->
                    onPainLevelChange(value.roundToInt())
                },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = painLevel.toString(),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun SymptomChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(text)
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFFF3F4F6),
            labelColor = Color(0xFF374151),
            selectedContainerColor = Color(0xFFEA580C),
            selectedLabelColor = Color.White
        )
    )
}
