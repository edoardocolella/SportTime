package com.example.polito_mad_01.repositories


import com.example.polito_mad_01.daos.UserDao
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun userById(userId: Int): Flow<User>  = userDao.getUserById(userId)
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    fun updateUser(user: User) = userDao.updateUser(user)

}