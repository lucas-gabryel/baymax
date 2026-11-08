package com.ifal.baymax.db.general

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GeralTriagemDao {

    @Insert
    suspend fun inserir(triagem: GeralTriagem)

    @Query("SELECT * FROM geral_triagem ORDER BY criadoEm DESC")
    suspend fun buscarTodas(): List<GeralTriagem>
}