package com.example.polito_mad_01.model

import android.net.Uri
import androidx.room.ColumnInfo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user")
data class User(

   @PrimaryKey(autoGenerate = true)
   @ColumnInfo(name = "user_id")
   val userId: Int,

   @ColumnInfo(name = "name")
   val name: String,

   @ColumnInfo(name = "surname")
   val surname: String,

   @ColumnInfo(name = "nickname")
   var nickname: String,

   @ColumnInfo(name = "description")
   var description: String,

   @ColumnInfo(name = "gender")
   var gender: String,

   @ColumnInfo(name = "birthdate")
   var birthdate: String,

   @ColumnInfo(name = "location")
   var location: String,

   @ColumnInfo(name = "email")
   var email: String,

   @ColumnInfo(name = "phone_number")
   var phoneNumber: String,

   @ColumnInfo(name = "image_uri")
   var image_uri: String,

   @ColumnInfo(name = "monday_availability")
   var mondayAvailability: String,

   @ColumnInfo(name = "tuesday_availability")
   var tuesdayAvailability: String,

   @ColumnInfo(name = "wednesday_availability")
   var wednesdayAvailability: String,

   @ColumnInfo(name = "thursday_availability")
   var thursdayAvailability: String,

   @ColumnInfo(name = "friday_availability")
   var fridayAvailability: String,

   @ColumnInfo(name = "saturday_availability")
   var saturdayAvailability: String,

   @ColumnInfo(name = "sunday_availability")
   var sundayAvailability: String,
){
   override fun toString(): String{
      return "name:$name"
   }
}