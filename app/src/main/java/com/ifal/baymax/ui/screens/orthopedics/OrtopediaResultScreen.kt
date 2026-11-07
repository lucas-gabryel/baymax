package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ifal.baymax.db.orthopedics.OrtopediaTriage

@Composable
fun OrtopediaResultScreen(
    painLevel: Int,
    selectedSymptoms: Set<String>,
    details: String,
    history: List<OrtopediaTriage>,
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
            UrgencyCard(onViewRoute = onViewRoute)
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

        Text(
            text = "Histórico de Triagens",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(history, key = { it.id }) { triage ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Triagem #${triage.id} - Dor nível ${triage.painLevel}")
                        Text("Região: ${triage.affectedBone}")
                        if (triage.details.isNotBlank()) {
                            Text("Detalhes: ${triage.details}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UrgencyCard(
    onViewRoute: () -> Unit
) {
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
            OutlinedButton(
                onClick = onViewRoute,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text("Buscar hospital próximo")
            }
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
                    text = "Unidades recomendadas",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onViewRoute
                    ) {
                        Text("Buscar hospital próximo")
                    }
                }
            }
        }
    }
}