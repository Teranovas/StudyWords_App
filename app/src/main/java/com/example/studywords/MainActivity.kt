package com.example.studywords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.example.studywords.community.BoardActivity
import com.example.studywords.dictionary.DictionaryActivity
import com.example.studywords.sentence.SplashSentenceActivity
import com.example.studywords.translate.TranslateActivity
import com.example.studywords.word.WordChainActivity


class MainActivity : AppCompatActivity() {

    lateinit var communityBtn : CardView
    lateinit var sentenceBtn : CardView
    lateinit var wordBtn : CardView
    lateinit var translateBtn : CardView
    lateinit var dictionaryBtn : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        communityBtn = findViewById(R.id.communityCard)
        communityBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)

        })

        sentenceBtn = findViewById(R.id.sentenceCard)
        sentenceBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,SplashSentenceActivity::class.java)
            startActivity(intent)
        })

        wordBtn = findViewById(R.id.wordCard)
        wordBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WordChainActivity::class.java)
            startActivity(intent)

        })

        translateBtn = findViewById(R.id.translationCard)
        translateBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, TranslateActivity::class.java)
            startActivity(intent)

        })

        dictionaryBtn = findViewById(R.id.dictionaryCard)
        dictionaryBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DictionaryActivity::class.java)
            startActivity(intent)

        })


    }
}