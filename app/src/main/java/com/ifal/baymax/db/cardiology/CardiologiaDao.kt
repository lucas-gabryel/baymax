package com.ifal.baymax.db.cardiology

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardiologiaDao {

    @Insert
    fun salvarTriagem(record: CardiologiaRecord)

    @Query("SELECT * FROM CardiologiaRecord ORDER BY id DESC")
    fun buscarTodasAsTriagens(): List<CardiologiaRecord>
}