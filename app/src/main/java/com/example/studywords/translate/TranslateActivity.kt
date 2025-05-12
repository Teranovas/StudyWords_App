package com.example.studywords.translate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.studywords.R
import com.example.studywords.databinding.ActivityMainBinding
import com.example.studywords.databinding.ActivityTranslateBinding

class TranslateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTranslateBinding
    private val viewModel: TranslationViewModel by viewModels()

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

        val languageList = languageMap.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.sourceSpinner.adapter = adapter
        binding.targetSpinner.adapter = adapter

        binding.btnTranslate.setOnClickListener {
            val source = binding.sourceSpinner.selectedItem.toString()
            val target = binding.targetSpinner.selectedItem.toString()
            val input = binding.inputText.text.toString()

            viewModel.translateText(input, languageMap[source] ?: "", languageMap[target] ?: "")
        }

        viewModel.translatedText.observe(this, Observer {
            binding.outputText.text = it
        })
    }
}