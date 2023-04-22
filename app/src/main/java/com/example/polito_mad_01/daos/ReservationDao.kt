package com.example.polito_mad_01.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.polito_mad_01.model.Reservation
import com.example.polito_mad_01.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from reservation")
    fun getAllReservations(): Flow<List<Reservation>>

    /*@Query("SELECT * from reservation " +
            "INNER JOIN playground ON reservation.playground_id = playground.playground_id" +
            "where reservation_id = :reservationId")
    fun getReservationById(reservationId: Int): LiveData<Reservation>

    @Query("SELECT * from reservation " +
            "INNER JOIN reservation_extra ON reservation_extra.reservation_id = reservation.reservation_id" +
            "where user_id = :userId")
    fun getUserReservation(userId: Int): LiveData<Reservation>*/
}

