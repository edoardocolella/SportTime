package com.example.polito_mad_01.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.polito_mad_01.model.Playground
import com.example.polito_mad_01.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaygroundDao {
    @Query("SELECT * from playground")
    fun getAllPlaygrounds(): Flow<List<Playground>>

    @Query("SELECT * from user where playground_id = :playgroundId")
    fun getPlaygroundById(playgroundId: Int): LiveData<Playground>
}

