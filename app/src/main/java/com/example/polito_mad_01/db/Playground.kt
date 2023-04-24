package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playground",
    foreignKeys = [
        ForeignKey(
            entity = Sport::class,
            parentColumns = arrayOf("sport_name"),
            childColumns = arrayOf("sport_name"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Playground(
    @PrimaryKey(autoGenerate = true)
    val playground_id: Int = 0,
    val name: String,
    val description: String,
    val location: String,
    val price_per_slot: Double,
    val sport_name: String,
)