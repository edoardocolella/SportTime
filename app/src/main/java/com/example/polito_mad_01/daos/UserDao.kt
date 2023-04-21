package com.example.polito_mad_01.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.model.UserWithSkills
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from user where user_id = :userId")
    fun getUserById(userId: Int): LiveData<User>

    @Query("SELECT * from user")
    fun getAllUsers(): Flow<List<User>>

    @Transaction
    @Query("SELECT * " +
            "from user " +
            "INNER JOIN sport_skill ON user.user_id = sport_skill.user_id " +
            "where user.user_id = :userId")
    fun getUserByIdWithSkills(userId: Int): LiveData<UserWithSkills>

@Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)


}

