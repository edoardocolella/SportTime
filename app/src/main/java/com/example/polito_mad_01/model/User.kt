package com.example.polito_mad_01.model

import android.net.Uri

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val surname: String,
    val nickname: String,
    val description: String,
    val gender: String,
    val birthdate: Date,
    val email: String,
    val phoneNumber: String,
    val imageUri: Uri,
    val location: String,
    val mondayAvailability: String,
    val tuesdayAvailability: String,
    val wednesdayAvailability: String,
    val thursdayAvailability: String,
    val fridayAvailability: String,
    val saturdayAvailability: String,
    val sundayAvailability: String,
)