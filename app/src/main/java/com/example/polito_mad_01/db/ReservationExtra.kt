package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservation_extra",
    foreignKeys = [
        ForeignKey(
            entity = Slot::class,
            parentColumns = arrayOf("slot_id"),
            childColumns = arrayOf("slot_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaygroundExtra::class,
            parentColumns = arrayOf("extra_id"),
            childColumns = arrayOf("extra_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReservationExtra(
    @PrimaryKey(autoGenerate = true)
    val reservation_extra_id: Int = 0,
    val extra_id: Int,
    val slot_id: Int,
    )