package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "playground")
data class Playground(
    @PrimaryKey(autoGenerate = true)
    val playground_id: Int = 0,
    val name: String,
    val description: String,
    val location: String,
    val price_per_slot: Double,
    val sport_name: String,
)