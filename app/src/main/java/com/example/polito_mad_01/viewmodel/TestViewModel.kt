package com.example.polito_mad_01.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.polito_mad_01.model.UserRepository
import com.example.polito_mad_01.model.UserWithSkills
import kotlin.concurrent.thread

class TestViewModel(application: Application): AndroidViewModel(application) {

    private val repo = UserRepository(application)

    fun getUser() : String{
        val user = repo.user().value
        print("TEST")
        if(user != null) {
            return user.toString()
        } else {
            return "ERROR"
        }
    }

    fun addUser() {
        thread {
        repo.addUser()
            print("ADD")
        }
    }

}