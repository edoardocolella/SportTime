package com.example.polito_mad_01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CourtDao {
    @Insert
    fun insertReservation(reservation: Reservation)

    @Query("SELECT * FROM reservation")
    fun getALlReservations(): List<Reservation>

    @Delete
    fun deleteReservation(reservation: Reservation)
}

