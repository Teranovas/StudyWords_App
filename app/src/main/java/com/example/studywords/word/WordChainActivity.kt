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
import java.util.Locale

class WordChainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordChainBinding
    private val viewModel: WordViewModel by viewModels()
    private lateinit var tts: TextToSpeech
    private lateinit var adapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordChainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TTS 초기화
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.US
            }
        }

        // 어댑터 연결
        adapter = WordAdapter(emptyList()) { word ->
            speakWord(word)
        }
        binding.rvWord.adapter = adapter
        binding.rvWord.layoutManager = LinearLayoutManager(this)

        // ViewModel 관찰
        viewModel.wordList.observe(this) {
            adapter = WordAdapter(it) { word -> speakWord(word) }
            binding.rvWord.adapter = adapter
            binding.rvWord.scrollToPosition(it.size - 1)
        }

        // 단어 입력
        binding.btnSend.setOnClickListener {
            val input = binding.etInput.text.toString().trim()
            if (input.isNotEmpty()) {
                val error = viewModel.submitWord(input, true)
                if (error != null) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                } else {
                    binding.etInput.setText("")
                    gptRespond()
                }
            }
        }
    }

    private fun speakWord(word: String) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
    }
}
