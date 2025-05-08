package com.example.studywords.sentence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SentenceViewModel : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> get() = _messages

    private val systemPrompt = "너는 친근하고 일상적인 말투로 사용자와 대화하는 친구야. 말끝에 간단한 감탄사나 이모지를 섞어도 좋아."

    fun sendUserMessage(userInput: String) {
        val updated = _messages.value!!.toMutableList().apply {
            add(ChatMessage(userInput, isUser = true))
        }
        _messages.value = updated

        viewModelScope.launch {
            val lang = detectLanguage(userInput)
            val prompt = buildPrompt(systemPrompt, userInput, lang)

            val response = ChatRepository.sendMessage(prompt)
            val botMessage = ChatMessage(response ?: "응답을 받을 수 없습니다.", isUser = false)

            _messages.postValue(_messages.value!!.toMutableList().apply { add(botMessage) })
        }
    }

    private fun buildPrompt(systemPrompt: String, userInput: String, lang: String): String {
        val langName = getLanguageName(lang)
        return """
            You are a friendly AI who speaks in a natural, casual tone.
            Speak like a human friend, using the language: $langName.
            System: $systemPrompt
            User: $userInput
        """.trimIndent()
    }

    private fun detectLanguage(text: String): String {
        return when {
            Regex("[\\u3040-\\u30FF]").containsMatchIn(text) -> "ja"
            Regex("[a-zA-Z]").containsMatchIn(text) -> "en"
            Regex("[가-힣]").containsMatchIn(text) -> "ko"
            else -> "en"
        }
    }

    private fun getLanguageName(code: String): String {
        return when (code) {
            "en" -> "English"
            "ko" -> "Korean"
            "ja" -> "Japanese"
            else -> "English"
        }
    }
}