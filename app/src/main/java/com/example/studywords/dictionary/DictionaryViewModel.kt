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
        val prompt = """
        영어 단어 "$word"의 뜻을 한국어로 설명해줘.
        영어를 절대로 섞지 말고, 한글로만 작성해줘.
        마지막에 한글 예문도 한 문장 추가해줘.
    """.trimIndent()

        viewModelScope.launch(Dispatchers.IO) {
            val result = ChatGPTService.sendMessage(prompt)
            withContext(Dispatchers.Main) {
                _definition.value = result ?: "정의 불가"
            }
        }
    }
}