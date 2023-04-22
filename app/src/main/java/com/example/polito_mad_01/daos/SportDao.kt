package com.example.polito_mad_01.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.polito_mad_01.model.Playground
import com.example.polito_mad_01.model.Sport
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {
    @Query("SELECT * from sport")
    fun getAllSports(): Flow<List<Sport>>

    @Query("SELECT * from sport where sport_id = :sportId")
    fun getSportById(sportId: Int): LiveData<Sport>
}

