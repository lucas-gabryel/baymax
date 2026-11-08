package com.ifal.baymax.db.general

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geral_triagem")
data class GeralTriagem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nivelDor: Int,
    val sintomas: String,
    val detalhes: String,
    val criadoEm: Long = System.currentTimeMillis(),
)