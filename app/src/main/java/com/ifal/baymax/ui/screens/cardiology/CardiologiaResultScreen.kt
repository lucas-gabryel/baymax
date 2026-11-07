package com.ifal.baymax.ui.screens.cardiology

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ifal.baymax.api.cardiology.CardiologiaApiService

@Composable
fun CardiologiaResultScreen(
    navController: NavController,
    nivelDeDor: Int,
    sintoma: String
) {
    var recomendacao by remember { mutableStateOf("Buscando recomendação...") }
    var erro by remember { mutableStateOf(false) }

    val nivelTexto = when {
        nivelDeDor >= 8 -> "Alto"
        nivelDeDor >= 5 -> "Moderado"
        else -> "Leve"
    }

    LaunchedEffect(Unit) {
        try {
            val urlGithub = "https://raw.githubusercontent.com/lucas-gabryel/baymax/main/api_cardio.json"
            val lista = CardiologiaApiService.create().buscarRecomendacoes(urlGithub)

            val resultado = lista.find {
                it.nivel == nivelTexto && it.sintoma == sintoma
            }

            recomendacao = resultado?.recomendacao ?: "Recomendação não encontrada para esta combinação."
        } catch (e: Exception) {
            erro = true
            recomendacao = "Erro ao conectar com a API: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        //1
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF9C1141),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.weight(1.5f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Olá, paciente!", color = Color.White, fontSize = 14.sp)
                    Text("Como podemos ajudar?", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💓", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cardiologia", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(
                text = "💗",
                fontSize = 100.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        //2
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            //1
            Text("Resultado da Triagem", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            //2
            Text("Nível de dor: $nivelDeDor ($nivelTexto)", fontSize = 18.sp)
            Text("Sintoma: $sintoma", fontSize = 18.sp, modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(24.dp))

            //3
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Recomendação:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recomendacao,
                        color = if (erro) Color.Red else Color(0xFF9C1141),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        //3
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF9C1141)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Voltar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}