package com.example.polito_mad_01.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * from slot")
    fun getAllReservations(): Flow<List<Slot>>

    @Query("SELECT * from slot where user_id = :user_id and is_reserved = true")
    fun getReservationByUserId(user_id: Int): Flow<List<Slot>>
    

}

