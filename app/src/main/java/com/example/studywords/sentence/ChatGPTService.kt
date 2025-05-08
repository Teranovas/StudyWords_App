package com.example.studywords.sentence

import android.telecom.Call
import com.google.android.gms.common.api.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.security.auth.callback.Callback

object ChatGPTService {
    private const val API_KEY = "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"
    private const val API_URL = "https://api.openai.com/v1/chat/completions"

    fun sendMessage(prompt: String): String? {
        val client = OkHttpClient()

        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo")
        val messages = JSONArray()
        messages.put(JSONObject().put("role", "system").put("content", "You are a friendly AI that talks casually like a human."))
        messages.put(JSONObject().put("role", "user").put("content", prompt))
        json.put("messages", messages)

        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $API_KEY")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            response.body?.let {
                val jsonResponse = JSONObject(it.string())
                jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()
            }
        } else {
            println("❌ 실패 응답 코드: ${response.code}")
            println("❌ 실패 응답 내용: ${response.body?.string()}")
            null
        }
    }
}