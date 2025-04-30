package com.example.studywords.community

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studywords.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private val viewModel: PostViewModel by viewModels()
    private val adapter = PostAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerPosts.adapter = adapter
        binding.recyclerPosts.layoutManager = LinearLayoutManager(this)

        // 게시글 실시간 업데이트
        viewModel.postList.observe(this) { posts ->
            adapter.submitList(posts)
        }

        // 게시글 작성 버튼 클릭

    }
}





