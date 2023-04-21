package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playground",
    foreignKeys = [
        ForeignKey(
            entity = Sport::class,
            parentColumns = arrayOf("sport_id"),
            childColumns = arrayOf("sport_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Playground(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "playground_id")
    val playgroundId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "price_per_slot")
    val price_per_slot: Double,

    @ColumnInfo(name = "sport_id")
    val sport_id: Int,
)