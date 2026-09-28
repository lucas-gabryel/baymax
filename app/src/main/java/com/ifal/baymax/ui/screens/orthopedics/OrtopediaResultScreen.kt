package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrtopediaResultScreen(
    painLevel: Int,
    selectedSymptoms: Set<String>,
    details: String,
    onNewTriage: () -> Unit
) {
    val isUrgent = painLevel >= 8

    val symptomsText = if (selectedSymptoms.isEmpty()) {
        "Nenhum sintoma selecionado"
    } else {
        selectedSymptoms.joinToString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Resultado da triagem")

        if (isUrgent) {
            UrgencyCard()
        } else {
            NormalRecommendationCard()
        }

        Text("Nível de dor registrado: $painLevel")

        Text("Sintomas: $symptomsText")

        if (details.isNotBlank()) {
            Text("Outros detalhes: $details")
        }

        Button(
            onClick = onNewTriage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Nova triagem")
        }
    }
}

@Composable
private fun UrgencyCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4E6)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Atenção necessária",
                style = MaterialTheme.typography.titleLarge
            )

            Text("O nível de dor informado requer atenção imediata.")
        }
    }
}

@Composable
private fun NormalRecommendationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF7ED)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Análise concluída",
                style = MaterialTheme.typography.titleLarge
            )

            Text("Seu quadro foi registrado para orientação de atendimento.")
        }
    }
}