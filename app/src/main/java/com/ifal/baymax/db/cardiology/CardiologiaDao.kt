package com.ifal.baymax.db.cardiology

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardiologiaDao {

    @Insert
    suspend fun salvarTriagem(record: CardiologiaRecord)

    @Query("SELECT * FROM CardiologiaRecord ORDER BY id DESC")
    suspend fun buscarTodasAsTriagens(): List<CardiologiaRecord>
}