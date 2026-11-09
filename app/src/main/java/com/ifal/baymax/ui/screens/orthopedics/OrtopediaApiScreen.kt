package com.ifal.baymax.ui.screens.orthopedics

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.ifal.baymax.api.orthopedics.HospitalElement

@SuppressLint("MissingPermission")
@Composable
fun OrtopediaApiScreen(
    hospitals: List<HospitalElement>,
    isLoading: Boolean,
    errorMessage: String?,
    onLoadHospitals: (Double, Double) -> Unit,
    onLocationError: (String) -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var isFetchingLocation by remember { mutableStateOf(true) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { loc ->
                    isFetchingLocation = false
                    if (loc != null) {
                        onLoadHospitals(loc.latitude, loc.longitude)
                    } else {
                        onLocationError("Não foi possível obter a localização do aparelho. Verifique se o GPS está ativado.")
                    }
                }
                .addOnFailureListener {
                    isFetchingLocation = false
                    onLocationError("Falha ao obter a localização do aparelho.")
                }
        } else {
            isFetchingLocation = false
            onLocationError("Permissão de localização negada. Ative o GPS para buscar hospitais próximos.")
        }
    }
    LaunchedEffect(Unit) {
        isFetchingLocation = true
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Hospitais Mais Próximos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text("Toque em um hospital para abrir a rota no Google Maps")
        when {
            isFetchingLocation || isLoading -> {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(errorMessage, color = Color.Red, modifier = Modifier.weight(1f))
            }
            hospitals.isEmpty() -> {
                Text("Nenhum hospital encontrado próximo.", modifier = Modifier.weight(1f))
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(hospitals, key = { it.id }) { hospital ->
                        val name = hospital.name?.ifBlank { null } ?: "Hospital / UPA"
                        val address = hospital.displayName ?: "Ver endereço no Maps"
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("🏥 $name", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("📍 $address")
                                Button(
                                    onClick = {
                                        val uri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${hospital.lat},${hospital.lon}")
                                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C))
                                ) {
                                    Text("Abrir rota no Google Maps")
                                }
                            }
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar")
        }
    }
}