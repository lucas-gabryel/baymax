package com.ifal.baymax.ui.screens.geral

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifal.baymax.db.general.GeralDatabase
import com.ifal.baymax.db.general.GeralTriagem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GeralHistoricoScreen() {
    val context = LocalContext.current

    var triagens by remember { mutableStateOf(listOf<GeralTriagem>()) }
    var carregando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        triagens = GeralDatabase.getInstance(context).triagemDao().buscarTodas()
        carregando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Histórico de triagens",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = GeralVermelhoEscuro
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                carregando -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                triagens.isEmpty() -> Text(
                    text = "Nenhuma triagem registrada ainda.",
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(triagens) { triagem ->
                        CardTriagem(triagem)
                    }
                }
            }
        }
    }
}

@Composable
private fun CardTriagem(triagem: GeralTriagem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Dor ${triagem.nivelDor}/10",
                fontWeight = FontWeight.Bold,
                color = GeralVermelhoEscuro
            )

            Text(formatarData(triagem.criadoEm), fontSize = 12.sp, color = Color.Gray)
            Text("Sintomas: ${triagem.sintomas.ifEmpty { "nenhum" }}")
            if (triagem.detalhes.isNotBlank()) {
                Text("Detalhes: ${triagem.detalhes}")
            }
        }
    }
}

private fun formatarData(millis: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"))
    return formato.format(Date(millis))
}