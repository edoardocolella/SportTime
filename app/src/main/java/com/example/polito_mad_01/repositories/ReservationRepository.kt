package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.db.ReservationDao
import com.example.polito_mad_01.db.*
import kotlinx.coroutines.flow.Flow

class ReservationRepository(private val reservationDao: ReservationDao) {

    fun getAllReservations(): Flow<List<Slot>> = reservationDao.getAllReservations()

    fun getReservationByUserId(userID: Int) : Flow<List<SlotWithPlayground>> {
        return reservationDao.getReservationByUserId(userID)
    }

    fun getSlotsByUserId(userID: Int) : Flow<List<SlotWithPlayground>> {
        return reservationDao.getSlotsByUserId(userID)
    }

    fun getOldReservationsByUserId(user_id: Int, date: String): Flow<List<SlotWithPlayground>>{
        return reservationDao.getOldReservationsByUserId(user_id, date)
    }

    fun getOldReservationById(id: Int): Flow<SlotWithPlayground>{
        return reservationDao.getOldReservationById(id)
    }

    fun getReservationById(id: Int): Flow<SlotWithPlayground> {
        return reservationDao.getReservationById(id)
    }

    fun getFreeSlots(date: String): Flow<List<SlotWithPlayground>>{
        return reservationDao.getFreeSlots(date)
    }

    fun updateReservation(slot: Slot) = reservationDao.updateReservation(slot)
    fun getFreeSlotsByPlayground(playgroundID: Int, today: String): Flow<List<SlotWithPlayground>> {
        return reservationDao.getPlaygroundFreeSlots(playgroundID, today)
    }

    fun getSlotByStartEndTime(originalStartTime: String, originalEndTime: String, date:String, playgroundID: Int): Flow<Slot> {
        return reservationDao.getSlotByStartEndTime(originalStartTime, originalEndTime,date, playgroundID)
    }
}