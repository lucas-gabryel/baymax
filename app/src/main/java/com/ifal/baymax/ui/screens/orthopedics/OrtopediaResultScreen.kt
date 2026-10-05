package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrtopediaResultScreen(
    painLevel: Int,
    selectedSymptoms: Set<String>,
    details: String,
    onNewTriage: () -> Unit,
    onViewRoute: () -> Unit
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
            NormalRecommendationCard(
                painLevel = painLevel,
                symptomsText = symptomsText,
                onViewRoute = onViewRoute
            )
        }

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4E6)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "⚠\uFE0F Atenção necessária",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "O nível de dor informado requer atenção imediata."
            )
        }
    }
}

@Composable
private fun NormalRecommendationCard(
    painLevel: Int,
    symptomsText: String,
    onViewRoute: () -> Unit
) {
    Column {
        Text(
            text = "Análise concluída",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Nível de dor informado: $painLevel"
        )

        Text(
            text = "Sintomas selecionados: $symptomsText"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF7ED)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Unidade recomendada",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "UBS Centro",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Unidade de Saúde de referência - distância estimada"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("\uD83D\uDCCD 850 metros")

                    OutlinedButton(
                        onClick = onViewRoute
                    ) {
                        Text("Ver rota")
                    }
                }
            }
        }
    }
}