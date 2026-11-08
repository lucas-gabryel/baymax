package com.ifal.baymax.ui.screens.geral

import android.R
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
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import com.ifal.baymax.db.general.GeralDatabase
import com.ifal.baymax.db.general.GeralTriagem
import com.ifal.baymax.ui.navigation.Screen
import kotlinx.coroutines.launch
import java.nio.channels.Selector
import kotlin.math.roundToInt

val GeralVermelho = Color(0xFFEF4444)
val GeralVermelhoEscuro = Color(0xFFDC2626)
val GeralVermelhoClaro = Color(0xFFFEE3E3)

private val sintomasComuns = listOf("Febre", "Dor de cabeça", "Enjoo", "Dorde garganta")

@Composable
private fun GeralHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(GeralVermelho, GeralVermelhoEscuro)))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "\uD83E\uDE7A", fontSize = 40.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Clínica Geral",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Assistente de triagem",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun CardNivelDor(
    nivelDor: Int,
    onNivelDorChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Qual o seu nível de dor?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Slider(
                value = nivelDor.toFloat(),
                onValueChange = {onNivelDorChange(it.roundToInt())},
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(top = 12.dp),
                colors = SliderDefaults.colors(
                    thumbColor = GeralVermelhoEscuro,
                    activeTickColor = GeralVermelhoEscuro
                )
            )

            Text(
                text = nivelDor.toString(),
                color = GeralVermelhoEscuro,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun SintomasChips(
    selecionados: Set<String>,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Sintomas comuns",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        FlowRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sintomasComuns.forEach { sintoma ->
                FilterChip(
                    selected = sintoma in selecionados,
                    onClick = { onToggle(sintoma)},
                    label = { Text(sintoma)},
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GeralVermelhoClaro
                    )
                )
            }
        }
    }
}

@Composable
fun GeralScreen(navController: NavController) {
    var nivelDor by remember { mutableIntStateOf(5) }
    var sintomas by remember { mutableStateOf(setOf<String>()) }
    var detalhes by remember { mutableStateOf("")}
    var salvando by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        GeralHeader()

        CardNivelDor(
            nivelDor = nivelDor,
            onNivelDorChange = { nivelDor = it }
        )

        SintomasChips(
            selecionados = sintomas,
            onToggle = { sintoma ->
                sintomas = if (sintoma in sintomas) sintomas - sintoma else sintomas + sintoma
            }
        )

        OutlinedTextField(
            value = detalhes,
            onValueChange = { detalhes = it },
            label = { Text("Outros detalhes (opcional") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            minLines = 3,
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = {
                salvando = true
                scope.launch {
                    GeralDatabase.getInstance(context).triagemDao().inserir(
                        GeralTriagem(
                            nivelDor = nivelDor,
                            sintomas = sintomas.joinToString { ", " },
                            detalhes = detalhes
                        )
                    )
                    salvando = false
                    navController.navigate(Screen.GeralResultado.criarRota(nivelDor))
                }
            },
            enabled = !salvando,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GeralVermelhoEscuro)
        ) {
            Text("Avaliar quadro", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = { navController.navigate(Screen.GeralHistorico.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Ver histórico de triagens")
        }
    }
}
