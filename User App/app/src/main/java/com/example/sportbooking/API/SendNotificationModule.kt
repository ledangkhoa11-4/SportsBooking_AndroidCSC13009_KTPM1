package com.example.sportbooking.API

import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotificationModule {
    companion object{
        fun createRequestBody(to: String, title: String, body: String): RequestBody {
            val mediaType =
                "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = """
        {
            "to": "${to}",
            "notification": {
                "title": "${title}",
                "body": "${body}"
            }
        }
    """.trimIndent()
            val reqBody = RequestBody.create(mediaType, requestBody)
            Log.i("AAAAAAAA",requestBody)

            return reqBody
        }

        fun sendNotification(to:String, title:String, body:String){
            val apiInterface = NotificationAPI.create()
                .getResources(
                    createRequestBody(
                        to,
                        title,
                        body
                    )
                )
            apiInterface.enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    Log.i("Notification Sent",response.toString())
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.i("Error on sending notification",t.toString())
                }
            })
        }
    }
}