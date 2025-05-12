package com.example.studywords.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studywords.sentence.ChatGPTService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DictionaryViewModel : ViewModel() {
    private val _definition = MutableLiveData<String>()
    val definition: LiveData<String> get() = _definition

    fun lookupDefinition(word: String, language: String) {
        val prompt = if (language == "한국어") {
            """
        다음 영어 단어 '$word'의 의미를 한국어로 설명해줘.
        절대로 영어로 설명하지 말고, 한국어로만 설명해줘.
        그리고 한글 예문도 한 문장 포함해줘.
        """.trimIndent()
        } else {
            "Define the English word '$word' in $language. Include a simple explanation and example sentence."
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = ChatGPTService.sendMessage(prompt)
            withContext(Dispatchers.Main) {
                _definition.value = result ?: "정의 불가"
            }
        }
    }
}