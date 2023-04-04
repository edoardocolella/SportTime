package com.example.polito_mad_01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM reservation")
    fun getAllUsers(): List<Reservation>

    @Delete
    fun deleteReservation(reservation: Reservation)
}

