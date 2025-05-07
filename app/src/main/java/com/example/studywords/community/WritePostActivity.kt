package com.example.studywords.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.studywords.R
import com.example.studywords.databinding.ActivityBoardBinding
import com.example.studywords.databinding.ActivityPostDetailBinding
import com.example.studywords.databinding.ActivityWritePostBinding

class WritePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWritePostBinding
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWritePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }

        val writeBtn = findViewById<android.widget.Button>(R.id.writeBtn)

        binding.writeBtn.setOnClickListener(View.OnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()
            if (title.isNotBlank() && content.isNotBlank()) {
                viewModel.addPost(title, content, "익명")
                Toast.makeText(this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this, "제목과 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            }

        })
    }
}