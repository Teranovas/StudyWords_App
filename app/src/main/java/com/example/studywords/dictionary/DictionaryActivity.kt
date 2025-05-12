package com.example.studywords.dictionary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.studywords.R
import com.example.studywords.databinding.ActivityDictionaryBinding
import java.util.Locale

class DictionaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDictionaryBinding
    private val viewModel: DictionaryViewModel by viewModels<DictionaryViewModel>()

    private lateinit var tts: TextToSpeech

    private val languageMap = mapOf(
        "영어" to Locale.ENGLISH,
        "한국어" to Locale.KOREAN,
        "일본어" to Locale.JAPANESE,
        "중국어" to Locale.SIMPLIFIED_CHINESE,
        "아랍어" to Locale("ar"),
        "라틴어" to Locale("la")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val languageList = languageMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.targetSpinner.adapter = adapter

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.ENGLISH
            }
        }

        binding.btnSearch.setOnClickListener {
            val word = binding.inputText.text.toString()
            val targetLang = binding.targetSpinner.selectedItem.toString()
            viewModel.lookupDefinition(word, targetLang)
        }

        viewModel.definition.observe(this, Observer {
            binding.outputText.text = it
        })

        binding.micIcon.setOnClickListener {
            val text = binding.outputText.text.toString()
            val lang = binding.targetSpinner.selectedItem.toString()
            val locale = languageMap[lang] ?: Locale.ENGLISH

            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = locale
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Toast.makeText(this, "해당 언어는 음성 출력을 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
