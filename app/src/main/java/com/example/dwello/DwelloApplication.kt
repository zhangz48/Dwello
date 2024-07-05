package com.example.dwello

import android.app.Application
import com.example.dwello.data.UserDatabase
import com.example.dwello.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class DwelloApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { UserDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { UserRepository(database.userDao()) }


}