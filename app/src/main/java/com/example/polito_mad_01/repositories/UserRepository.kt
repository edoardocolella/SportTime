package com.example.polito_mad_01.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.polito_mad_01.daos.UserDao
import com.example.polito_mad_01.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun userById(userId: Int): Flow<User> {
        return userDao.getUserById(userId)
    }

    val users: Flow<List<User>> = userDao.getAllUsers()

    @WorkerThread
    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

}