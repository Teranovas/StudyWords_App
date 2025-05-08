package com.example.studywords.sentence


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ChatRepository {

    suspend fun sendMessage(prompt: String): String? {
        return withContext(Dispatchers.IO) {
            ChatGPTService.sendMessage(prompt)
        }
    }
}