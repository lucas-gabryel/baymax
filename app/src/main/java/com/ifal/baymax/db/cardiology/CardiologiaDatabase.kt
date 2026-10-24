package com.ifal.baymax.db.cardiology

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

@Database(
    version = 1, entities = [CardiologiaRecord::class]
)
abstract class CardiologiaDatabase : RoomDatabase() {

    abstract fun cardiologiaDao(): CardiologiaDao

    companion object {
        fun getInstance(context: Context): CardiologiaDatabase {
            return Room.databaseBuilder(
                context,
                CardiologiaDatabase::class.java,
                "baymax_cardio.db"
            ).allowMainThreadQueries().build()
        }
    }
}