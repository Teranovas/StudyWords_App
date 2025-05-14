package com.example.studywords.translate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.studywords.R
import com.example.studywords.databinding.ActivityMainBinding
import com.example.studywords.databinding.ActivityTranslateBinding
import java.util.Locale

class TranslateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTranslateBinding
    private val viewModel: TranslationViewModel by viewModels()
    private lateinit var tts: TextToSpeech

    private val languageMap = mapOf(
        "한국어" to "Korean",
        "영어" to "English",
        "일본어" to "Japanese",
        "중국어" to "Chinese",
        "아랍어" to "Arabic",
        "라틴어" to "Latin"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 스피너 초기화
        val languageList = languageMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.sourceSpinner.adapter = adapter
        binding.targetSpinner.adapter = adapter

        // TTS 초기화
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.ENGLISH
            }
        }

        binding.btnBack.setOnClickListener { finish() }

        // 번역 버튼 클릭 시
        binding.btnTranslate.setOnClickListener {
            val source = binding.sourceSpinner.selectedItem.toString()
            val target = binding.targetSpinner.selectedItem.toString()
            val input = binding.inputText.text.toString()

            viewModel.translateText(input, languageMap[source] ?: "", languageMap[target] ?: "")
        }

        // 번역된 결과 관찰
        viewModel.translatedText.observe(this, Observer {
            binding.outputText.text = it
        })

        // 마이크(음성 재생) 클릭 시
        binding.micIcon.setOnClickListener {
            val text = binding.outputText.text.toString()
            val targetLangName = binding.targetSpinner.selectedItem.toString()
            val locale = getLocaleFromLanguageName(targetLangName)

            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.language = locale
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Toast.makeText(this, "해당 언어는 음성 출력을 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocaleFromLanguageName(languageName: String): Locale {
        return when (languageName) {
            "영어", "English" -> Locale.ENGLISH
            "한국어", "Korean" -> Locale.KOREAN
            "일본어", "Japanese" -> Locale.JAPANESE
            "중국어", "Chinese" -> Locale.SIMPLIFIED_CHINESE
            "아랍어", "Arabic" -> Locale("ar")
            "라틴어", "Latin" -> Locale("la")
            else -> Locale.ENGLISH
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