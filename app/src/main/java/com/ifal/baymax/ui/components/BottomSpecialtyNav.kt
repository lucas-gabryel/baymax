package com.ifal.baymax.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ifal.baymax.ui.triage.Specialties
import com.ifal.baymax.ui.triage.Specialty

@Composable
fun BottomSpecialtyNav(
    current: Specialty,
    onSelect: (Specialty) -> Unit
) {
    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.White) {
        Specialties.forEach { specialty ->
            val selected = specialty.key == current.key
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(specialty) },
                icon = { Icon(specialty.icon, contentDescription = specialty.title) },
                label = { Text(specialty.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = specialty.color,
                    selectedTextColor = specialty.color,
                    indicatorColor = specialty.colorSoft
                )
            )
        }
    }
}
