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
    @ColumnInfo(name = "reservation_extra_id")
    val reservationExtraId: Int = 0,

    @ColumnInfo(name = "extra_id")
    val extraId: Int,

    @ColumnInfo(name = "reservation_id")
    val reservationId: Int,



    )