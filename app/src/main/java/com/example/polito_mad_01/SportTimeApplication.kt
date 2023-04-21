package com.example.polito_mad_01

import android.app.Application
import com.example.polito_mad_01.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class SportTimeApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { SportTimeDatabase.getDatabase(this, applicationScope) }
    val userRepository by lazy { UserRepository(database.userDao()) }
}