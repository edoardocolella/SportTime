package com.example.polito_mad_01.model

import android.net.Uri

class User(
    val id: String,
    val name: String,
    val surname: String,
    val nickname : String,
    val description : String,
    val gender : String,
    val birthdate : String,
    val email: String,
    val phoneNumber: String,
    val location: String,
    val imageUri: Uri,
    val expertList: List<String>,
    val intermediateList: List<String>,
    val beginnerList: List<String>,
    val mondayAvailability: String,
    val tuesdayAvailability: String,
    val wednesdayAvailability: String,
    val thursdayAvailability: String,
    val fridayAvailability: String,
    val saturdayAvailability: String,
    val sundayAvailability: String
    )
{}