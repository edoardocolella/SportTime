package com.example.polito_mad_01.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.Review
import com.google.firebase.firestore.FirebaseFirestore

class ReviewRepository() {
    fun getSingleReview(userId: String, playgroundId: Int): LiveData<Review> {
        val toReturn = MutableLiveData<Review>()
        FirebaseFirestore.getInstance().collection("reviews")
            .whereEqualTo("user_id",userId)
            .whereEqualTo("playground_id", playgroundId)
            .get()
            .addOnSuccessListener { returnedReviews, ->
                toReturn.value = returnedReviews.documents[0].toObject(Review::class.java) ?: Review()
            }
        return toReturn
    }

    fun addReview(reviewToAdd: Review){
        FirebaseFirestore.getInstance().collection("reviews")
            .add(reviewToAdd)
    }

}