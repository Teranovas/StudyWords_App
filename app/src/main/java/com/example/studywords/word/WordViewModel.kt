package com.example.studywords.word

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale

class WordViewModel {
    private val _wordList = MutableLiveData<MutableList<WordItem>>(mutableListOf())
    val wordList: LiveData<MutableList<WordItem>> get() = _wordList

    private val usedWords = mutableSetOf<String>()

    fun submitWord(word: String, isUser: Boolean): String? {
        val normalizedWord = word.lowercase(Locale.getDefault())

        if (usedWords.contains(normalizedWord)) return "이미 사용한 단어입니다!"

        if (_wordList.value!!.isNotEmpty()) {
            val lastWord = _wordList.value!!.last().word
            if (lastWord.last() != normalizedWord.first()) {
                return "단어의 첫 글자가 맞지 않습니다!"
            }
        }

        _wordList.value!!.add(WordItem(normalizedWord, isUser))
        _wordList.postValue(_wordList.value)
        usedWords.add(normalizedWord)
        return null
    }

    fun getLastChar(): Char? = _wordList.value?.lastOrNull()?.word?.last()
    fun getUsedWords(): Set<String> = usedWords
}