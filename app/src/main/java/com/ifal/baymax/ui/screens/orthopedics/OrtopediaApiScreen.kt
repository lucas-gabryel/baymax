package com.ifal.baymax.ui.screens.orthopedics

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.ifal.baymax.api.orthopedics.HospitalElement

@SuppressLint("MissingPermission")
@Composable
fun OrtopediaApiScreen(
    userLat: Double,
    userLng: Double,
    hospitals: List<HospitalElement>,
    isLoading: Boolean,
    errorMessage: String?,
    onLoadHospitals: (Double, Double) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        onLoadHospitals(location.latitude, location.longitude)
                    } else {
                        onLoadHospitals(userLat, userLng)
                    }
                }
                .addOnFailureListener {
                    onLoadHospitals(userLat, userLng)
                }
        } else {
            onLoadHospitals(userLat, userLng)
        }
    }
    LaunchedEffect(Unit) {
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
        Text(
            text = "Toque em um hospital para abrir a rota no Google Maps",
            style = MaterialTheme.typography.bodyMedium
        )
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.weight(1f)
                )
            }
            hospitals.isEmpty() -> {
                Text(
                    text = "Nenhum hospital encontrado num raio de 10 km.",
                    modifier = Modifier.weight(1f)
                )
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(hospitals, key = { it.id }) { hospital ->
                        val hLat = hospital.latitude ?: 0.0
                        val hLon = hospital.longitude ?: 0.0
                        val distResult = FloatArray(1)
                        Location.distanceBetween(userLat, userLng, hLat, hLon, distResult)
                        val distanceKm = String.format("%.1f km", distResult[0] / 1000f)
                        val name = hospital.tags?.name ?: "Hospital / UPA"
                        val street = hospital.tags?.street ?: "Ver endereço no Maps"
                        val suburb = hospital.tags?.suburb ?: ""
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "🏥 $name",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("📍 $street ${if (suburb.isNotBlank()) "- $suburb" else ""}")
                                Text(
                                    text = "🚗 Distância aproximada: $distanceKm",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFEA580C)
                                )
                                Button(
                                    onClick = {
                                        val mapsUri = Uri.parse(
                                            "https://www.google.com/maps/dir/?api=1&destination=$hLat,$hLon"
                                        )
                                        context.startActivity(Intent(Intent.ACTION_VIEW, mapsUri))
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEA580C)
                                    )
                                ) {
                                    Text("Abrir rota no Google Maps")
                                }
                            }
                        }
                    }
                }
            }
        }
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar")
        }
    }
}