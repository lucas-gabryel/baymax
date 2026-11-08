package com.ifal.baymax.db.general

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [GeralTriagem::class], exportSchema = false)
abstract class GeralDatabase: RoomDatabase() {

    abstract fun triagemDao(): GeralTriagemDao

    companion object {
        fun getInstance(context: Context): GeralDatabase {
            return Room.databaseBuilder(
                context,
                GeralDatabase::class.java,
                "baymax_geral.db"
            ).build()
        }
    }
}