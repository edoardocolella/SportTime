package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "slot",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Playground::class,
            parentColumns = arrayOf("playground_id"),
            childColumns = arrayOf("playground_id"),
            onDelete = ForeignKey.CASCADE
        )
    ])

data class Slot(
    @PrimaryKey(autoGenerate = true)
    val slot_id: Int = 0,
    val playground_id: Int,
    val user_id: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val total_price: Double,
    val is_reserved: Boolean,
    )