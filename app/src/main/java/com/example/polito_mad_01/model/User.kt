package com.example.polito_mad_01.model

import android.net.Uri

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
class User(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "surname") val surname: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "birthdate") val birthdate: Date,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String,
    @ColumnInfo(name = "imageUri") val imageUri: Uri,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "mondayAvailability") val mondayAvailability: String,
    @ColumnInfo(name = "tuesdayAvailability") val tuesdayAvailability: String,
    @ColumnInfo(name = "wednesdayAvailability") val wednesdayAvailability: String,
    @ColumnInfo(name = "thursdayAvailability") val thursdayAvailability: String,
    @ColumnInfo(name = "fridayAvailability") val fridayAvailability: String,
    @ColumnInfo(name = "saturdayAvailability") val saturdayAvailability: String,
    @ColumnInfo(name = "sundayAvailability") val sundayAvailability: String,
    val expertList: List<String>,
    val intermediateList: List<String>,
    val beginnerList: List<String>
) {}