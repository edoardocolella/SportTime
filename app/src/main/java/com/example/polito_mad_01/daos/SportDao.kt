package com.example.polito_mad_01.daos

import androidx.room.*
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {
    @Query("SELECT * from sport")
    fun getAllSports(): Flow<List<Sport>>

    /*@Query("SELECT * from sport where sport_id = :sportId")
    fun getSportById(sportId: Int): LiveData<Sport>*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSport(sport: Sport)
}

