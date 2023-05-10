package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.db.Review
import com.example.polito_mad_01.repositories.ReviewRepository

class ReviewViewModel(private val repository: ReviewRepository): ViewModel(){
    lateinit var review: LiveData<Review>

    fun getSingleReview(user_id: Int, playground_id: Int): LiveData<Review>{
        review = repository.getSingleReview(user_id, playground_id).asLiveData()
        return review
    }
}

class ReviewViewModelFactory(private val repository: ReviewRepository): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}