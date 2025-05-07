package com.example.studywords.community

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studywords.R
import com.example.studywords.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private val viewModel: PostViewModel by viewModels()
//    private val adapter = PostAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter { post ->
            // 👉 게시글 클릭 시 상세화면으로 이동
            val intent = Intent(this, PostDetailActivity::class.java)
            intent.putExtra("postId", post.id)  // ← 게시글 ID 전달
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener { finish() }

        val writeBtn = findViewById<ImageButton>(R.id.writeBtn)

        writeBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, WritePostActivity::class.java)
            startActivity(intent)
            true
        })
//        writ
//        -> {
//            val intent = Intent(this, WritePostActivity::class.java)
//            startActivity(intent)
//            true
//        }

        binding.recyclerPosts.adapter = adapter
        binding.recyclerPosts.layoutManager = LinearLayoutManager(this)

        // 게시글 실시간 업데이트
        viewModel.postList.observe(this) { posts ->
            adapter.submitList(posts)
        }

        // 게시글 작성 버튼 클릭

    }
}





