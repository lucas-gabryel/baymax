package com.ifal.baymax.db.cardiology

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardiologiaRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nivelDeDor: Int,
    val sintomas: String,
    val outrosDetalhes: String
)