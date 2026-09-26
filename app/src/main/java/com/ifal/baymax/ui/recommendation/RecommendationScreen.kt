package com.ifal.baymax.ui.recommendation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ifal.baymax.ui.theme.CrisisBg
import com.ifal.baymax.ui.theme.CrisisBody
import com.ifal.baymax.ui.theme.CrisisIcon
import com.ifal.baymax.ui.theme.CrisisTitle
import com.ifal.baymax.ui.theme.EmergencyBg
import com.ifal.baymax.ui.theme.EmergencyBody
import com.ifal.baymax.ui.theme.EmergencyIcon
import com.ifal.baymax.ui.theme.EmergencyTitle
import com.ifal.baymax.ui.theme.SuccessBg
import com.ifal.baymax.ui.theme.SuccessText
import com.ifal.baymax.ui.triage.Outcome
import com.ifal.baymax.ui.triage.TriageResult
import com.ifal.baymax.ui.triage.TriageViewModel

@Composable
fun RecommendationScreen(
    viewModel: TriageViewModel,
    onNewTriage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val result = viewModel.result ?: return
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResultHeader(result)

        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (result.outcome) {
                Outcome.NORMAL -> NormalCard(result) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=UBS+posto+de+sa%C3%BAde")
                    )
                    context.startActivity(intent)
                }
                Outcome.EMERGENCY -> EmergencyCard {
                    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:192")))
                }
                Outcome.CRISIS -> CrisisCard {
                    context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:188")))
                }
            }
        }

        OutlinedButton(
            onClick = onNewTriage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
        ) {
            Text(
                text = "Nova triagem",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ResultHeader(result: TriageResult) {
    val (bg, icon) = when (result.outcome) {
        Outcome.NORMAL -> result.specialty.color to result.specialty.icon
        Outcome.EMERGENCY -> EmergencyIcon to Icons.Filled.Warning
        Outcome.CRISIS -> CrisisIcon to Icons.Filled.Favorite
    }
    val title = when (result.outcome) {
        Outcome.NORMAL -> "Análise concluída"
        Outcome.EMERGENCY -> "Atenção necessária"
        Outcome.CRISIS -> "Vamos com calma"
    }
    val description = when (result.outcome) {
        Outcome.NORMAL -> "Com base no nível ${result.painLevel} registrado para ${result.specialty.title} e nos sintomas descritos (${result.symptomsSummary}), localizamos a unidade mais próxima para o seu caso."
        Outcome.EMERGENCY -> "Nível ${result.painLevel} registrado em ${result.specialty.title}, com: ${result.symptomsSummary}."
        Outcome.CRISIS -> "Você registrou um nível ${result.painLevel} de ansiedade hoje, com: ${result.symptomsSummary}."
    }

    Box(
        modifier = Modifier
            .size(96.dp)
            .background(bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
    }
    Text(
        text = title,
        modifier = Modifier.padding(top = 16.dp),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Black
    )
    Text(
        text = description,
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Composable
private fun NormalCard(result: TriageResult, onSeeRoute: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Unidade recomendada", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .background(SuccessBg, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Aberto agora", color = SuccessText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = "UBS Centro",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Unidade de Saúde de referência · distância estimada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("850 metros", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = onSeeRoute,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Ver rota", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun EmergencyCard(onCallSamu: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = EmergencyBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = EmergencyTitle)
                Text(
                    text = "Procure atendimento imediato",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmergencyTitle
                )
            }
            Text(
                text = "Pelo nível registrado, seu quadro pode exigir atendimento de urgência. Não espere: vá ao pronto-socorro mais próximo ou ligue para o SAMU.",
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                color = EmergencyBody,
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = onCallSamu,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmergencyIcon, contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Call, contentDescription = null)
                Text("Ligar para o SAMU (192)", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun CrisisCard(onCallCvv: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CrisisBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = CrisisTitle)
                Text(
                    text = "Você não precisa passar por isso sozinho",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CrisisTitle
                )
            }
            Text(
                text = "O nível que você registrou sugere um momento difícil agora. O CVV oferece apoio emocional sigiloso, 24h por dia, por telefone ou chat.",
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                color = CrisisBody,
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = onCallCvv,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CrisisIcon, contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Call, contentDescription = null)
                Text("Ligar para o CVV (188)", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}
