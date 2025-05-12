package com.example.studywords.translate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studywords.sentence.ChatGPTService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslationViewModel : ViewModel() {
    private val _translatedText = MutableLiveData<String>()
    val translatedText: LiveData<String> get() = _translatedText

    fun translateText(input: String, sourceLang: String, targetLang: String) {
        val prompt = "Translate the following text from $sourceLang to $targetLang: $input"
        viewModelScope.launch(Dispatchers.IO) {
            val fullResponse = ChatGPTService.sendMessage(prompt)
            val coreResult = extractTranslation(fullResponse)
            withContext(Dispatchers.Main) {
                _translatedText.value = coreResult ?: "번역 실패"
            }
        }
    }

    private fun extractTranslation(response: String?): String? {
        if (response == null) return null

        // 1. 큰따옴표 안의 외국어 추출
        val quoteRegex = Regex("\"([^\"]+)\"")
        val match = quoteRegex.find(response)
        return match?.groups?.get(1)?.value ?: response.trim()
    }
}