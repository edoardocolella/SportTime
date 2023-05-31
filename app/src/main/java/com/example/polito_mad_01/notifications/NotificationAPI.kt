package com.example.polito_mad_01.notifications

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {
    companion object{
        const val CONTENT_TYPE = "application/json"
        const val API_KEY = "AAAAyYNE6lo:APA91bHK1um__grhp3hXYTah1dhCLnT6JS4huimLRhxjBbuldxwBG0P4DSHthYHG-DRwTf1UKwlRjNpQO4LhFBm8FZKxpyOIC5C1DyIP4wPA5KQMguasY17Xczpxr-MH_zyLkvc6itLG"
    }

    @Headers("Authorization: key=${API_KEY}", "Content-Type: $CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}