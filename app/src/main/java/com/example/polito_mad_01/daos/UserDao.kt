package com.example.polito_mad_01.daos

import androidx.room.*
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * from user where user_id = :userId LIMIT 1")
    fun getUserById(userId: Int): Flow<User>

    @Query("SELECT * from user")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)
}

