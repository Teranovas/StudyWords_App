package com.example.studywords.word

import WordAdapter
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studywords.R
import com.example.studywords.databinding.ActivityWordChainBinding
import com.example.studywords.sentence.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class WordChainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordChainBinding
    private val viewModel: WordViewModel by viewModels()
    private lateinit var adapter: WordAdapter
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordChainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = WordAdapter(emptyList()) { speakWord(it) }
        binding.rvWord.layoutManager = LinearLayoutManager(this)
        binding.rvWord.adapter = adapter

        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        }

        binding.btnSend.setOnClickListener {
            val inputWord = binding.etInput.text.toString().trim()
            if (inputWord.isNotEmpty()) {
                val error = viewModel.submitWord(inputWord, true)
                if (error != null) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                } else {
                    binding.etInput.setText("")
                    gptRespond()
                }
            }
        }

        viewModel.wordList.observe(this) {
            adapter = WordAdapter(it) { word -> speakWord(word) }
            binding.rvWord.adapter = adapter
            binding.rvWord.scrollToPosition(it.size - 1)
        }
    }

    private fun gptRespond() {
        val lastChar = viewModel.getLastChar() ?: return
        val usedWords = viewModel.getUsedWords()

        val prompt = """
            Give me one English word that starts with '${lastChar.uppercaseChar()}'
            and is not in this list: ${usedWords.joinToString(", ")}.
            Only return the word itself, no punctuation or explanation.
        """.trimIndent()

        CoroutineScope(Dispatchers.Main).launch {
            val result = ChatRepository.sendMessage(prompt)

            if (result == null) {
                Toast.makeText(this@WordChainActivity, "GPT 응답 실패!", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val gptWord = result.lowercase(Locale.getDefault()).trim()
            val error = viewModel.submitWord(gptWord, false)
            if (error != null) {
                Toast.makeText(this@WordChainActivity, "GPT가 규칙을 어겼습니다: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun speakWord(word: String) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
