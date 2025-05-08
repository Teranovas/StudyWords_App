package com.example.studywords.sentence

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.studywords.R
import com.example.studywords.sentence.SentenceActivity

class SplashSentenceActivity : AppCompatActivity() {

    private lateinit var sentence1: TextView
    private lateinit var sentence2: TextView
    private lateinit var nextButton: Button
    private var clickStage = 0 // 클릭 단계 추적


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_sentence)

        sentence1 = findViewById(R.id.sentence1)
        sentence2 = findViewById(R.id.sentence2)
        nextButton = findViewById(R.id.nextButton)

        val btnback = findViewById<ImageView>(R.id.btnBack)
        btnback.setOnClickListener(View.OnClickListener {
            finish()
        })

        // 첫 문장 애니메이션 표시
        sentence1.animate().alpha(1f).setDuration(1000).start()

        // 다음 버튼 클릭 시 다음 화면으로 이동
        nextButton.setOnClickListener {
            val intent = Intent(this, SentenceActivity::class.java) // 여기에 다음 액티비티 넣기
            startActivity(intent)
            finish()
        }

        // 전체 레이아웃 클릭 이벤트로 문장 순차 등장
        val rootLayout = findViewById<ConstraintLayout>(R.id.rootLayout)
        rootLayout.setOnClickListener {
            when (clickStage) {
                0 -> {
                    sentence2.animate().alpha(1f).setDuration(1000).start()
                    clickStage++
                }
                1 -> {
                    nextButton.visibility = View.VISIBLE // 보이게 하고
                    nextButton.alpha = 0f                 // 처음엔 투명하게 시작
                    nextButton.animate().alpha(1f).setDuration(1000).start()
                    clickStage++
                }
            }
        }
    }
}



