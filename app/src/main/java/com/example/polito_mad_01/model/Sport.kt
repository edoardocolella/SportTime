package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport")
data class Sport(
    @PrimaryKey
    @ColumnInfo(name = "sport_name")
    val sportName: String,

)