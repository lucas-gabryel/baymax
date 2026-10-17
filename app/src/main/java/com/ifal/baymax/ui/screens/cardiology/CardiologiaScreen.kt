package com.ifal.baymax.ui.screens.cardiology

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardiologiaScreen() {
    var nivelDeDor by remember { mutableStateOf(5f) }
    var outrosDetalhes by remember { mutableStateOf("") }

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
                    Text(
                        "Olá, paciente!",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        "Como podemos ajudar?",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
        ) {
            //1
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Qual o nível de desconforto no peito?",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Slider(
                        value = nivelDeDor, //o que aparece
                        onValueChange = { novoValor -> nivelDeDor = novoValor }, //atualiza o valor ao ir puxando a barra
                        valueRange = 0f..10f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF9C1141), //cor da listra que se move
                            activeTrackColor = Color(0xFF9C1141), //cor da barra preenchida
                            inactiveTrackColor = Color(0xFFE0E0E0), //cor da barra vazia
                            activeTickColor = Color.Transparent, //remove os pontinhos da esquerda
                            inactiveTickColor = Color.Transparent //remove os pontinhos da direita
                        )
                    )

                    Text(
                        text = "Nível de dor:",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${nivelDeDor.toInt()}",
                        color = Color(0xFF9C1141),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            //2
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Sintomas comuns",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Palpitação", "Falta de ar", "Pressão alta", "Tontura", "x").forEach { sintoma ->
                        FilterChip(
                            selected = false,
                            onClick = {},
                            label = { Text(sintoma) },
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }
            //3
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Outros detalhes (opcional)",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = outrosDetalhes, //o que aparece
                    onValueChange = { texto -> outrosDetalhes = texto }, //atualiza o valor a cada letra digitada
                    placeholder = { Text("Descreva algo que os sintomas acima não cobrem...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF9C1141), //cor da borda quando clica
                        cursorColor = Color(0xFF9C1141), //cor do ponteiro de texto
                        unfocusedBorderColor = Color.LightGray //cor da borda quando não está clicado
                    )
                )
            }
            //4
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C1141)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Avaliar quadro",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}