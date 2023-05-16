package com.example.polito_mad_01.repositories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.db.FirebaseUser
import com.example.polito_mad_01.db.User
import com.example.polito_mad_01.db.UserDao
import com.example.polito_mad_01.db.UserWithSkills
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    private var user = MutableLiveData<FirebaseUser>()

    fun userById(userId: Int): Flow<User> =  userDao.getUserById(userId)

    fun userWithSkillsById(userId: Int): Flow<UserWithSkills> = userDao.getUserWithSkillsById(userId)

    fun firebaseUser(userID : String): LiveData<FirebaseUser> {
        FirebaseFirestore.getInstance().collection("users")
            .document(userID)
            .addSnapshotListener { r, e ->
                user.value =  r?.toObject(FirebaseUser::class.java)
            }
        return user
    }

    fun updateUserWithSkills(user: UserWithSkills){
        userDao.updateUser(user.user)
        userDao.updateSkills(user.skillList)
    }

}