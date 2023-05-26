package com.example.polito_mad_01.repositories


import androidx.lifecycle.*
import com.example.polito_mad_01.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserRepository{

    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()

    fun getUser(): LiveData<User> {
        val userID = fAuth.currentUser?.uid ?: ""
        val user = MutableLiveData<User>()
        fs.collection("users")
            .document(userID)
            .addSnapshotListener { r, _ ->
                user.value =  r?.toObject(User::class.java)
            }
        return user
    }

    fun updateUser(user: User) {
        val userID = fAuth.currentUser?.uid ?: ""
        fs.collection("users")
            .document(userID)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { println("UPDATE User updated") }
    }

}