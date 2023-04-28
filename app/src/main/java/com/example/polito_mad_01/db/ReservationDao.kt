package com.example.polito_mad_01.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from slot")
    fun getAllReservations(): Flow<List<Slot>>

    @Query("SELECT * from slot where user_id = :user_id and is_reserved = true")
    fun getReservationByUserId(user_id: Int): Flow<List<Slot>>

    /** @param date format: yyyy-MM-dd*/
    @Transaction
    @Query("SELECT * from slot " +
            "INNER JOIN playground ON slot.playground_id = playground.playground_id " +
            "where is_reserved = false " +
            "and date > :date")
    fun getFreeSlots(date:String): Flow<List<SlotWithPlayground>>

}

