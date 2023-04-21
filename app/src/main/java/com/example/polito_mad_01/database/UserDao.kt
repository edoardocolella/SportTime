package com.example.polito_mad_01.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.model.UserWithSkills

@Dao
interface UserDao {

    @Query("SELECT * from user where user_id = :userId")
    fun getUserById(userId: Int): User

    @Query("SELECT * " +
            "from user " +
            "INNER JOIN sport_skill ON user.user_id = sport_skill.user_id " +
            "where user_id = :userId")
    fun getUserByIdWithSkills(userId: Int): LiveData<UserWithSkills>


}

