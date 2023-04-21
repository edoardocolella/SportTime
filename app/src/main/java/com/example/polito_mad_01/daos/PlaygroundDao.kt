package com.example.polito_mad_01.daos

import androidx.room.Dao
import androidx.room.Query
import com.example.polito_mad_01.model.Playground
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaygroundDao {
    @Query("SELECT * from playground")
    fun getAllPlaygrounds(): Flow<List<Playground>>
}

