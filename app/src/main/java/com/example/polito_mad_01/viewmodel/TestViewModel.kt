package com.example.polito_mad_01.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.polito_mad_01.model.UserRepository
import com.example.polito_mad_01.model.UserWithSkills

class TestViewModel(application: Application): AndroidViewModel(application) {

    private val repo = UserRepository(application)
    val user = repo.user()


    fun getUser(): UserWithSkills{
        return user.value!!;
    }

}