package com.example.polito_mad_01.db

import androidx.room.*
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaygroundDao {
    @Query("SELECT * from playground")
    fun getAllPlaygrounds(): Flow<List<Playground>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPlayground(playground: Playground)

}

