package com.example.polito_mad_01.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Date,
    val startTime: String,
    val endTime: String,
    val price: Double,
    val playground: Playground,
    val user: User
    )