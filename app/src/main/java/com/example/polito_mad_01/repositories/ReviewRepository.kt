package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.db.Review
import com.example.polito_mad_01.db.ReviewDao
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val reviewDao: ReviewDao) {

    fun getAllReviews(): Flow<List<Review>>{
        return reviewDao.getAllReviews()
    }

    fun getReviewsByUserId(user_id: Int): Flow<List<Review>>{
        return reviewDao.getReviewsByUserId(user_id)
    }

    fun getSingleReview(user_id: Int, playground_id: Int): Flow<Review>{
        return reviewDao.getSingleReview(user_id, playground_id)
    }
}