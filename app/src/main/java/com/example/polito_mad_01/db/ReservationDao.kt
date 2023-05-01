package com.example.polito_mad_01.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from slot")
    fun getAllReservations(): Flow<List<Slot>>

    @Transaction
    @Query("SELECT * from slot WHERE slot.user_id = :user_id or user_id IS NULL")
    fun getReservationByUserId(user_id: Int): Flow<List<SlotWithPlayground>>

    /** @param date format: yyyy-MM-dd*/
    @Transaction
    @Query("SELECT * from slot " +
            "INNER JOIN playground ON slot.playground_id = playground.playground_id " +
            "where user_id is null " +
            "and date > :date")
    fun getFreeSlots(date:String): Flow<List<SlotWithPlayground>>

    @Query("DELETE from slot WHERE slot_id = :slot_id")
    fun deleteSlot(slot_id: Int) : Int

    @Query("SELECT * from slot WHERE slot.slot_id = :slot_id")
    fun getReservationById(slot_id: Int): Flow<SlotWithPlayground>

    @Update
    fun updateReservation(slot: Slot)

    @Transaction
    @Query("SELECT * from slot " +
            "INNER JOIN playground ON slot.playground_id = playground.playground_id " +
            "where user_id is null " +
            "and date > :today " +
            "and slot.playground_id = :playgroundID")
    fun getPlaygroundFreeSlots(playgroundID: Int, today: String): Flow<List<SlotWithPlayground>>


    @Query("SELECT * from slot " +
            "WHERE slot.start_time = :originalStartTime " +
            "and slot.end_time = :originalEndTime " +
            "and slot.date = :date " +
            "and slot.playground_id = :playgroundID")
    fun getSlotByStartEndTime(originalStartTime: String, originalEndTime: String, date: String, playgroundID: Int): Flow<Slot>

}

