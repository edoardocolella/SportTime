package com.example.polito_mad_01.repositories


import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.example.polito_mad_01.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.net.URI

class UserRepository{

    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

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

    fun getProfileImage(): LiveData<Uri?> {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val storageReference = storage.reference
        val imageRef = storageReference.child("profileImages/$userID.jpg")
        val localFile = File.createTempFile("images", "jpg")
        val image = MutableLiveData<Uri?>()
        imageRef.getFile(localFile).addOnSuccessListener {
            image.value = localFile.toUri()
        }.addOnFailureListener {
            println("Error while downloading image")
            image.value = null
        }
        return image
    }

    fun updateProfileImage(imageUri: Uri) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val storageReference = storage.reference
        val imageRef = storageReference.child("profileImages/$userID.jpg")
        imageRef.putFile(imageUri).addOnSuccessListener {
            println("Image uploaded")
        }.addOnFailureListener {
            throw Exception("Error while uploading image")
        }
    }

    fun getFriendNickname(id: String): LiveData<String> {
        val user = MutableLiveData<String>()
        fs.collection("users")
            .document(id)
            .addSnapshotListener { r, _ ->
                user.value =  r?.get("nickname").toString()
            }
        return user
    }

}