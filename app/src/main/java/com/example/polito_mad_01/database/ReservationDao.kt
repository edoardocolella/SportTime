package com.example.polito_mad_01.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReservationDao {
    @Insert
    fun insertReservation(reservation: Reservation){
        var overlappingReservations = getReservationsByUserAndCourt(reservation.user_id,reservation.court_id)
            .filterNot { (it.start < reservation.start && it.end< reservation.end) || (it.start > reservation.start && it.end > reservation.end) }
        if(overlappingReservations != emptyList<Reservation>()){
            //throw Error
        }else{
            //insert query
        }
    }

    @Query("SELECT * FROM reservation")
    fun getAllReservations(): List<Reservation>

    @Query("SELECT * FROM reservation WHERE court_id = :courtId")
    fun getReservationsByCourt(courtId: Int): List<Reservation>

    @Query("SELECT * FROM reservation WHERE user_id = :userId")
    fun getReservationsByUser(userId: Int): List<Reservation>

    @Query("SELECT * FROM reservation WHERE user_id = :userId AND court_id = :courtId")
    fun getReservationsByUserAndCourt(userId: Int, courtId: Int): List<Reservation>

    @Delete
    fun deleteReservation(reservation: Reservation)
}

