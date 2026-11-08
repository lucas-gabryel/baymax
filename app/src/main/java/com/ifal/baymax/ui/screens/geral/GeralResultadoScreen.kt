package com.ifal.baymax.ui.screens.geral

import android.R
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ifal.baymax.api.general.GeralRetrofitHelper
import com.ifal.baymax.api.general.UnidadeSaude

private const val CODIGO_ARAPIRACA = 270030
private const val TIPO_UBS = 2
private const val TIPO_PRONTO_ATENDIMENTO = 73

@Composable
fun GeralResultadoScreen(
    nivelDor: Int,
    onNovaTriagem: () -> Unit,
    onVerHistorico: () -> Unit
) {
    val urgente = nivelDor >= 8

    var unidades by remember { mutableStateOf(listOf<UnidadeSaude>()) }
    var carregando by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf(false) }
    var tentativa by remember { mutableIntStateOf(0) }

    LaunchedEffect(tentativa) {
        carregando = true
        erro = false
        try {
            val resposta = GeralRetrofitHelper().unidadeSaudeApi.buscarUnidades(
                codigoMunicipio = CODIGO_ARAPIRACA,
                codigoTipoUnidade = if (urgente) TIPO_PRONTO_ATENDIMENTO else TIPO_UBS
            )
            unidades = resposta.estabelecimentos
        } catch (error: Exception) {
            Log.e("GeralResultado", "Falha ao buscar unidades", error)
            erro = true
        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CardRecomendacao(nivelDor = nivelDor, urgente = urgente)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                carregando -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GeralVermelhoEscuro
                )

                erro -> Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Não foi possível carregar as unidades")
                    OutlinedButton(onClick = { tentativa++}) {
                        Text("Tentar novamente")
                    }
                }

                unidades.isEmpty() -> Text(
                    text = "Nenhuma unidade encontrada.",
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(unidades) {
                        unidade -> CardUnidade(unidade)
                    }
                }
            }
        }

        Button(
            onClick = onNovaTriagem,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = GeralVermelhoEscuro)
        ) {
            Text("Nova triagem")
        }

        OutlinedButton(
            onClick = onVerHistorico,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver histórico de triagens")
        }
    }
}

@Composable
private fun CardRecomendacao(nivelDor: Int, urgente: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GeralVermelhoClaro)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (urgente) "Procure atendimento urgente" else "Procure uma UBS",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GeralVermelhoEscuro
            )
            Text("Nível de dor informado: $nivelDor")
        }
    }
}

@Composable
private fun CardUnidade(unidade: UnidadeSaude) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = unidade.nome ?: "Unidade sem nome",
                fontWeight = FontWeight.Bold
            )
            Text("${unidade.endereco.orEmpty()}, ${unidade.numero.orEmpty()}")
            Text("Bairro: ${unidade.bairro.orEmpty()}")
            unidade.telefone?.let { telefone ->
                Text("Telefone: $telefone")
            }
        }
    }
}