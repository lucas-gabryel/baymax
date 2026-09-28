package com.ifal.baymax.ui.screens.orthopedics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrtopediaScreen() {
    var painLevel by remember {
        mutableIntStateOf(5)
    }

    var selectedSymptoms by remember {
        mutableStateOf(setOf<String>())
    }

    var details by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OrtopediaHeader()

        Text("Ortopedia")
    }
}

@Composable
private fun OrtopediaHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFB923C),
                        Color(0xFFEA580C)
                    )
                )
            )
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "🦴",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Ortopedia",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Assistente de triagem",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp
            )
        }
    }
}
