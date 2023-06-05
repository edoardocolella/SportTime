package com.example.polito_mad_01.repositories


import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.notifications.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.io.File


class UserRepository{

    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun getUser(): LiveData<User> {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val user = MutableLiveData<User>()
        fs.collection("users")
            .document(userID)
            .addSnapshotListener { r, _ ->
                user.value =  r?.toObject(User::class.java)
            }
        return user
    }

    fun updateUser(user: User) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("users")
            .document(userID)
            .set(user, SetOptions.merge())
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
            image.value = null
        }
        return image
    }

    fun updateProfileImage(imageUri: Uri) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val storageReference = storage.reference
        val imageRef = storageReference.child("profileImages/$userID.jpg")
        imageRef.putFile(imageUri)
        .addOnFailureListener {
            throw Exception("Error while uploading image")
        }
    }

    fun getUserFriends() : LiveData<List<User>>{
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val liveDataList = MutableLiveData<List<User>>()

        fs.collection("users")
            .document(userID)
            .addSnapshotListener { r, _ ->
                val friendsIds = r?.toObject(User::class.java)?.friends

                if (friendsIds != null && friendsIds.isNotEmpty()) {
                    fs.collection("users")
                        .get()
                        .addOnSuccessListener { query ->
                            val list = query.documents
                                .filter { friendsIds.contains(it.id) }
                                .map {
                                    it.toObject(User::class.java)!!
                                }
                            liveDataList.value = list
                        }
                }
            }

        return liveDataList
    }

    fun getFriendsNickname(idList: List<String>): LiveData<List<Pair<String,String>>> {
        val nicknameList = MutableLiveData<List<Pair<String,String>>>()
        fs.collection("users")
            .get()
            .addOnSuccessListener { result ->
                nicknameList.value = result.documents
                    .filter { idList.contains(it.id) }
                    .map { Pair(it.id,it["nickname"].toString()) }
            }
        return nicknameList
    }

    fun createUser(user: User, uuid: String){
        fs.collection("users")
            .document(uuid)
            .set(user)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addFriend(email: String): LiveData<String> {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val toBeReturned = MutableLiveData<String>()
        fs.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    toBeReturned.value = "noAccount"
                } else {
                    fs.collection("users")
                        .document(userID).get()
                        .addOnSuccessListener {
                            if (!it["friends"].toString().contains(result.documents[0].id)) {
                                fs.collection("friendRequests")
                                    .document("${userID}-${result.documents[0].id}")
                                    .set(mapOf("sender" to userID, "receiver" to result.documents[0].id))
                                    .addOnSuccessListener {
                                        GlobalScope.launch (Dispatchers.IO){
                                            val data = NotificationData("Friend request",
                                                "You have a new friend request from ${fAuth.currentUser?.email}",
                                                "friendRequests")
                                            val push = PushNotification(data, "/topics/${result.documents[0].id}")
                                            sendNotification(push)
                                        }
                                    }
                                toBeReturned.value = result.documents[0].id
                            }
                            else {
                                toBeReturned.value = "alreadyFriend"
                            }
                        }
                }
            }
        return toBeReturned
    }

    fun getRequestsUUID(): LiveData<List<String>>{
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        val requests = MutableLiveData<List<String>>()
        fs.collection("friendRequests")
            .whereEqualTo("receiver", userID)
            .addSnapshotListener { result, _ ->
                requests.value = result?.documents?.map { it["sender"].toString() }
            }
        return requests
    }

    fun acceptRequest(senderUUID: String) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")

        fs.collection("users")
            .document(userID)
            .update("friends", FieldValue.arrayUnion(senderUUID))

        fs.collection("users")
            .document(senderUUID)
            .update("friends", FieldValue.arrayUnion(userID))

        fs.collection("friendRequests")
            .document("$senderUUID-$userID")
            .delete()
    }

    fun declineRequest(senderUUID: String) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("friendRequests")
            .document("$senderUUID-$userID")
            .delete()
    }


     private suspend fun sendNotification(notification: PushNotification) {
         APIManager().postNotification(notification)
    }

    fun subscribeToNotifications() {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        FirebaseMessaging.getInstance().subscribeToTopic(userID)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    println("Subscribed to friend requests notifications")
                else
                    println("Error while subscribing to friend requests notifications ${it.exception}")
            }
    }

    fun logout() {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        FirebaseMessaging.getInstance().unsubscribeFromTopic(userID)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    println("Unsubscribed from friend requests notifications")
                else
                    println("Error while unsubscribing from friend requests notifications ${it.exception}")
            }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendGameRequest(email: String, slot: Slot) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                    fs.collection("gameRequests")
                        .document("${userID}-${result.documents[0].id}-${slot.slot_id}")
                        .set(mapOf("sender" to userID, "receiver" to result.documents[0].id, "slotID" to slot.slot_id, "date" to slot.date))
                        .addOnSuccessListener{
                            GlobalScope.launch (Dispatchers.IO){
                                val data = NotificationData("Game request",
                                    "You have a new game request from ${fAuth.currentUser?.email}",
                                    "gameRequests")
                                val push = PushNotification(data, "/topics/${result.documents[0].id}")
                                sendNotification(push)
                            }
                        }
            }
    }

    fun findFriendsBySkillAndLocation(skillName: String, skillValue:String, location: String): LiveData<List<User>> {
        val liveDataList = MutableLiveData<List<User>>()
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("users")
            .whereEqualTo("skills.$skillName", skillValue)
            //.whereEqualTo("location", location)
            .get()
            .addOnSuccessListener { query ->
                liveDataList.value = query.documents.filter { it.id != userID }
                    .filter { !(it["friends"] as List<*>).contains(userID) }
                    .filter { it["location"].toString().lowercase() == location.lowercase()}
                    .map {
                    it.toObject(User::class.java)!!
                }
            }
        return liveDataList
    }

}