package com.ifal.baymax.db.orthopedics

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [OrtopediaTriage::class],
    version = 1,
    exportSchema = false
)
abstract class OrtopediaDatabase : RoomDatabase() {
    abstract fun ortopediaDao(): OrtopediaDao
    companion object {
        @Volatile
        private var INSTANCE: OrtopediaDatabase? = null
        fun getDatabase(context: Context): OrtopediaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrtopediaDatabase::class.java,
                    "ortopedia_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}