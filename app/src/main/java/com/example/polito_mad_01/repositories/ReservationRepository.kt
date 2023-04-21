package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.daos.ReservationDao
import com.example.polito_mad_01.model.Reservation
import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDao: ReservationDao) {

    val reservations: Flow<List<Reservation>> = reservationDao.getAllReservations()

}