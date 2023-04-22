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

    @Transaction
    @Query(
        "SELECT * " +
                "from user " +
                "INNER JOIN sport_skill ON user.user_id = sport_skill.user_id " +
                "INNER JOIN sport ON sport_skill.sport_name = sport.sport_name " +
                "where user.user_id = :userId " +
                "LIMIT 1"
    )
    fun getUserWithSkillsById(userId: Int): Flow<UserWithSkills>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM user U, sport_skill SS " +
                "WHERE U.user_id = SS.user_id"
    )
    fun getAllUsersWithSkills(): Flow<List<UserWithSkills>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("DELETE FROM sport_skill WHERE user_id = :userId")
    fun deleteSkills(userId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSkills(skills: List<SportSkill>)

    @Update
    fun updateUser(user: User)


}

