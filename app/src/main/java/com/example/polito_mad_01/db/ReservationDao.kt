package com.example.polito_mad_01.db

import androidx.room.*
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from reservation")
    fun getAllReservations(): Flow<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addReservation(reservation: Reservation)

}

