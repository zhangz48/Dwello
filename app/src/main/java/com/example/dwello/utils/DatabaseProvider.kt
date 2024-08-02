package com.example.dwello.utils

import android.content.Context
import androidx.room.Room
import com.example.dwello.repositories.AppDatabase

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "property_database"
            )
//                .addMigrations(MIGRATION_1_2) // Add the migration here
                .build()
            INSTANCE = instance
            instance
        }
    }
}