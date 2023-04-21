package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reservations",
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

data class Reservation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reservation_id")
    val reservationId: Int = 0,

    @ColumnInfo(name = "playground_id")
    val playgroundId: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "start_time")
    val startTime: String,

    @ColumnInfo(name = "end_time")
    val endTime: String,

    @ColumnInfo(name = "total_price")
    val totalPrice: Double,


    )