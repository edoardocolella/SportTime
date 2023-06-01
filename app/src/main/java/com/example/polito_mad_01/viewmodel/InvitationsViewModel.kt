package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.repositories.InvitationRepository

class InvitationsViewModel(repository: InvitationRepository) : ViewModel(){

}

class InvitationsViewModelFactory(private val repository: InvitationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvitationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvitationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}