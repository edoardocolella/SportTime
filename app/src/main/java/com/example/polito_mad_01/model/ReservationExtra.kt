package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservation_extra",
    foreignKeys = [
        ForeignKey(
            entity = Reservation::class,
            parentColumns = arrayOf("reservation_id"),
            childColumns = arrayOf("reservation_id"),
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
    val reservation_id: Int,
    )