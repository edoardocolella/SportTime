package com.example.polito_mad_01.repositories


import com.example.polito_mad_01.daos.UserDao
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun userById(userId: Int): Flow<User>  = userDao.getUserById(userId)
    fun userWithSkillsById(userId: Int): Flow<UserWithSkills>  = userDao.getUserWithSkillsById(userId)

    val users: Flow<List<User>> = userDao.getAllUsers()
    val usersWithSkills: Flow<List<UserWithSkills>> = userDao.getAllUsersWithSkills()


    fun insertAllSkills(skills: List<SportSkill>) {
        userDao.insertAllSkills(skills)
    }

    fun updateUser(user: User) = userDao.updateUser(user)
    fun deleteSkills(userId: Int) = userDao.deleteSkills(userId)


}