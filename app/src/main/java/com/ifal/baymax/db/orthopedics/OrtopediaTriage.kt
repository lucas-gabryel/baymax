package com.ifal.baymax.db.orthopedics

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ortopedia_triage")
data class OrtopediaTriage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val painLevel: Int,
    val affectedBone: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)