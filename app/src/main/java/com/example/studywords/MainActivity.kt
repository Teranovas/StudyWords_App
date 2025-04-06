package com.example.studywords

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.example.studywords.community.BoardActivity

class MainActivity : AppCompatActivity() {

    lateinit var communityBtn : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        communityBtn = findViewById(R.id.communityCard)
        communityBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, BoardActivity::class.java)
            startActivity(intent)

        })


    }
}