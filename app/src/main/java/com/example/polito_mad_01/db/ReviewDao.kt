package com.example.polito_mad_01.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT * FROM review")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM review " +
            "WHERE user_id = :user_id"
    )
    fun getReviewsByUserId(user_id: Int): Flow<List<Review>>


    @Query("SELECT * FROM review " +
            "WHERE user_id = :user_id AND playground_id = :playground_id"
    )
    fun getSingleReview(user_id: Int, playground_id: Int): Flow<Review>
}