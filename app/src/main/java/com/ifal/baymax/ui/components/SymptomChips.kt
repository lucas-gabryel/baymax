package com.ifal.baymax.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SymptomChips(
    chips: List<String>,
    selectedChips: Set<String>,
    themeColor: Color,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips) { chip ->
            val selected = chip in selectedChips
            FilterChip(
                selected = selected,
                onClick = { onToggle(chip) },
                label = { Text(chip) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = themeColor,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
