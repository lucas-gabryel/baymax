package com.ifal.baymax.ui.screens.cardiology

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifal.baymax.db.cardiology.CardiologiaDatabase

@Composable
fun CardiologiaResultScreen() {
    val context = LocalContext.current

    val bancoDeDados = CardiologiaDatabase.getInstance(context)
    val listaTriagens = bancoDeDados.cardiologiaDao().buscarTodasAsTriagens()

    if (listaTriagens.isEmpty()) {
        Text(
            text = "Nenhuma triagem cadastrada.",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listaTriagens) { registro ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ID: ${registro.id}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text("Nível de Dor: ${registro.nivelDeDor}")
                    Text("Sintomas: ${registro.sintomas}")
                    Text("Detalhes: ${registro.outrosDetalhes}")
                }
            }
        }
    }
}