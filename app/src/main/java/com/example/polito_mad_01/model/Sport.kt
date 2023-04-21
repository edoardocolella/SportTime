package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport")
data class Sport(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sport_id")
    val sportId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

)