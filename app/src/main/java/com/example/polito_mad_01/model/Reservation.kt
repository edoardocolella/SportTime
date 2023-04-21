package com.example.polito_mad_01.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.polito_mad_01.database.Court
import java.util.Date

@Entity(tableName = "reservations",
    foreignKeys = [
        ForeignKey(
            entity = com.example.polito_mad_01.database.User::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Court::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("court_id"),
            onDelete = ForeignKey.CASCADE
        )
    ])

data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "start_time")
    val startTime: String,

    @ColumnInfo(name = "end_time")
    val endTime: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "playground")
    val playground: Playground,

    @ColumnInfo(name = "user")
    val user: User
    )