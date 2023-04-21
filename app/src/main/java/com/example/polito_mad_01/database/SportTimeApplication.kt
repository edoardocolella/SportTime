package com.example.polito_mad_01.database

import android.app.Application

class SportTimeApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { SportTimeDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
}