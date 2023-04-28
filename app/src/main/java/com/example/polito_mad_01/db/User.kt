package com.example.polito_mad_01.db

import androidx.room.*


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Int,
    var name: String,
    var surname: String,
    var nickname: String,
    var description: String,
    var gender: String,
    var birthdate: String,
    var location: String,
    var email: String,
    var phoneNumber: String,
    var image_uri: String?,
    var favouriteSport : String,
    var monday_availability: Boolean,
    var tuesday_availability: Boolean,
    var wednesday_availability: Boolean,
    var thursday_availability: Boolean,
    var friday_availability: Boolean,
    var saturday_availability: Boolean,
    var sunday_availability: Boolean
) {}