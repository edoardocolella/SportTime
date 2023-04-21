package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playgrounds")
data class Playground(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "sport")
    val sport: String,

    @ColumnInfo(name = "price")
    val price: Double
    )