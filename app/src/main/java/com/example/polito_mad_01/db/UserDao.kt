package com.example.polito_mad_01.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * from user " +
            "INNER JOIN skill ON user.user_id = skill.user_id " +
            "where user.user_id = :user_id")
    fun getUserWithSkillsById(user_id: Int): Flow<UserWithSkills>

    @Query("SELECT * from user " +
            "where user.user_id = :user_id")
    fun getUserById(user_id: Int): Flow<User>

    @Query("SELECT * from user")
    fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)
}

