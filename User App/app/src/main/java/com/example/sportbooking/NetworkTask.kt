package com.example.sportbooking

import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkTask(private val client: OkHttpClient, private val url: String): AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg p0: Void?): String? {
        val request = Request.Builder()
            .url(url)
            .build()
        try {
            val response = client.newCall(request).execute()
            return response.body?.string()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}