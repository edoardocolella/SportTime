package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.db.ReservationDao
import com.example.polito_mad_01.db.*
import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDao: ReservationDao) {

    fun getAllReservations(): Flow<List<Slot>> = reservationDao.getAllReservations()

fun getReservationByUserId(userID: Int) : Flow<List<SlotWithPlayground>> {
        return reservationDao.getReservationByUserId(userID)
    }
}