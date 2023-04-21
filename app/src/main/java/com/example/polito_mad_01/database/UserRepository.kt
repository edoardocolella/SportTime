package com.example.polito_mad_01.database

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.polito_mad_01.model.User

class UserRepository(private val userDao: UserDao) {
    fun user(): LiveData<User> = userDao.getUserById(0)

    fun addUser(user: User) {
            userDao.addUser(user)
    }

}