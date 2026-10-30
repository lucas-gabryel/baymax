package com.ifal.baymax.db.orthopedics

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrtopediaDao {

    @Insert
    suspend fun insert(triage: OrtopediaTriage)

    @Query("SELECT * FROM ortopedia_triage ORDER BY timestamp DESC")
    fun getAllTriages(): Flow<List<OrtopediaTriage>>
}