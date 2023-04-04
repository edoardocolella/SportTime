package com.example.polito_mad_01.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "reservation",
    foreignKeys = [
        ForeignKey(
        entity = User::class,
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
    ]
)
data class Reservation (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "court_id")
    val court_id: Int,

    @ColumnInfo(name = "players_number")
    val players_number: Int,

    @ColumnInfo(name = "start")
    val start: LocalDateTime,

    @ColumnInfo(name = "end")
    val end: LocalDateTime,
)

@Entity(tableName = "user")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "surname")
    val surname: String?,

    @ColumnInfo(name = "birthdate")
    val birthdate: Date?,

    @ColumnInfo(name = "gender")
    val gender: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "phone_number")
    val phone_number: String?
)

@Entity(
    tableName = "court",
    foreignKeys = [
        ForeignKey(
            entity = Sport::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("sport_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Court (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "sport")
    val name: Int,

    @ColumnInfo(name = "price_per_hour")
    val surname: Float,

    @ColumnInfo(name = "address")
    val birthdate: String
)

@Entity(tableName = "sport")
data class Sport (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String
)
