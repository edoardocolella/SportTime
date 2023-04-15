package com.example.polito_mad_01.model

import android.net.Uri

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
   var fullname: String,
   var nickname: String,
   var description: String,
    var gender: String,
   var age: Int,
   var email: String,
   var phoneNumber: String,
    var imageUriString: String,
   var location: String,
   var mondayAvailability: String,
   var tuesdayAvailability: String,
   var wednesdayAvailability: String,
   var thursdayAvailability: String,
   var fridayAvailability: String,
   var saturdayAvailability: String,
   var sundayAvailability: String,
){
    @PrimaryKey(autoGenerate = true)var id: Int = 0
}