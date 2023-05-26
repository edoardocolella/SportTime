package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.polito_mad_01.model.Review
import com.example.polito_mad_01.repositories.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: ReviewRepository): ViewModel(){
    lateinit var review: LiveData<Review>

    fun getSingleReview(playground_id: Int): LiveData<Review>{
        review = repository.getSingleReview(playground_id)//.asLiveData()
        return review
    }

    fun addReview(reviewToAdd: Review){
        viewModelScope.launch(Dispatchers.IO){
            repository.addReview(reviewToAdd)
        }
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