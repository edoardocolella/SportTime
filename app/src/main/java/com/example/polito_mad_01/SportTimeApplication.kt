package com.example.polito_mad_01

import android.app.Application
import com.example.polito_mad_01.repositories.*

class SportTimeApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    //private val database by lazy { SportTimeDatabase.getDatabase(this) }\
    val userRepository by lazy { UserRepository() }
    val reservationRepository by lazy { ReservationRepository() }
    val reviewRepository by lazy { ReviewRepository()    }
}