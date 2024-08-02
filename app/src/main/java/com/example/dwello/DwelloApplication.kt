package com.example.dwello

import android.app.Application
import androidx.room.Room
import com.example.dwello.repositories.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DwelloApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "dwello-database"
        ).fallbackToDestructiveMigration() // This is crucial to avoid migration errors
            .build()
    }
}