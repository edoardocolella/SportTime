package com.example.polito_mad_01.model

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.polito_mad_01.database.SportTimeDatabase

class UserRepository(application: Application) {

    private val userDao = SportTimeDatabase.getDatabase(application).userDao()



    fun user(): LiveData<User> = userDao.getUserById(0)
    fun addUser() {
        val user = User(
            0,
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test",
            "test")
            userDao.addUser(user)
    }

}