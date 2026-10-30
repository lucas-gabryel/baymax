package com.ifal.baymax.db.orthopedics

import kotlinx.coroutines.flow.Flow

class OrtopediaRepository(private val dao: OrtopediaDao) {

    val allTriages: Flow<List<OrtopediaTriage>> = dao.getAllTriages()

    suspend fun insert(triage: OrtopediaTriage) {
        dao.insert(triage)
    }
}