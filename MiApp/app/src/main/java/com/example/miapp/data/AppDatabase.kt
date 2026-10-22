package com.example.miapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miapp.model.ObjetivoAprendizaje

@Database(entities = [ObjetivoAprendizaje::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oaDao(): OaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mpaz_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}