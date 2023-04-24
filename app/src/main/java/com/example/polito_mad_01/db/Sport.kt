package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport")
data class Sport(
    @PrimaryKey
    val sport_name: String,
)