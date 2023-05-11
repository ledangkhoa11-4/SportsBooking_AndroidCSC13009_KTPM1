package com.example.sportbooking.API

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NotificationAPI
{
    @POST("fcm/send")
    fun getResources(
        @Body requestBody: RequestBody,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = "key=AAAAJY3Z1iU:APA91bGahHeYlEXsvTpquuI60vP3OTUWU7cJvrB-oU_o7i9EX1HSD8Coh3rj__-KU_D3Uy5IpDelO7dH0QC9phYmWzgE8ynJmhtR9UI81x1ycWaaZxMLe6jD7N-knrvdFgdFVd1pGeKv",

        ) : Call<List<String>>
    companion object {
        var BASE_URL = "https://fcm.googleapis.com/"
        fun create() : NotificationAPI {
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build()
            return retrofit.create(NotificationAPI::class.java)
        }
    }
}