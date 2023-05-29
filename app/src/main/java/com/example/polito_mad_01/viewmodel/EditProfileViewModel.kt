package com.example.polito_mad_01.viewmodel

import android.net.Uri
import android.view.View
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.UserRepository
import com.google.firebase.storage.FirebaseStorage
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var user : MutableLiveData<User>
    lateinit var chipGroup : LiveData<View>
     var imageUri = MutableLiveData<String>(null)

    fun getUser(): LiveData<User>{
        user = userRepository.getUser() as MutableLiveData<User>
        return user
    }

    fun updateUser() {
        thread {
            println("UPDATE updateUser ${imageUri.value}")
/*            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("storage/test/user.jpeg")
            val uploadTask = storageRef.putFile(Uri.parse(imageUri.value))*/

/*            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // L'immagine è stata caricata con successo
                    // Puoi accedere all'URL di download tramite task.result
                    val downloadUrl = task.result?.storage?.downloadUrl
                    println("DOWNLOAD AVAILABLE AT $downloadUrl")
                    // ...fai qualcos'altro con l'URL di download
                } else {
                    // Si è verificato un errore durante il caricamento dell'immagine
                    val exception = task.exception
                    // ...gestisci l'errore
                }
            }*/

            user.value?.let { userRepository.updateUser(it) }

        }
    }


}


class EditProfileViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}