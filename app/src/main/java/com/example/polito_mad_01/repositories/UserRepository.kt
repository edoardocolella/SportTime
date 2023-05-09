package com.example.polito_mad_01.repositories


import com.example.polito_mad_01.db.User
import com.example.polito_mad_01.db.UserDao
import com.example.polito_mad_01.db.UserWithSkills
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun userById(userId: Int): Flow<User> =  userDao.getUserById(userId)

    fun userWithSkillsById(userId: Int): Flow<UserWithSkills> = userWithSkillsById(userId)

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    fun updateUser(user: User) = userDao.updateUser(user)

    fun updateUserWithSkills(user: UserWithSkills){
        userDao.updateUser(user.user)
        userDao.updateSkills(user.skillList)
    }

}