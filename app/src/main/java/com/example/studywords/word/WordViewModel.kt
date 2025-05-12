package com.example.studywords.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class WordViewModel : ViewModel() {
    private val _wordList = MutableLiveData<MutableList<WordItem>>(mutableListOf())
    val wordList: LiveData<MutableList<WordItem>> get() = _wordList

    private val usedWords = mutableSetOf<String>()

    fun submitWord(word: String, isUser: Boolean): String? {
        val normalized = word.lowercase(Locale.getDefault())

        if (usedWords.contains(normalized)) return "이미 사용한 단어입니다!"

        if (_wordList.value!!.isNotEmpty()) {
            val last = _wordList.value!!.last().word
            if (last.last() != normalized.first()) {
                return "단어의 첫 글자가 맞지 않습니다!"
            }
        }

        _wordList.value!!.add(WordItem(normalized, isUser))
        _wordList.postValue(_wordList.value)
        usedWords.add(normalized)
        return null
    }

    fun getLastChar(): Char? = _wordList.value?.lastOrNull()?.word?.last()
    fun getUsedWords(): Set<String> = usedWords
}