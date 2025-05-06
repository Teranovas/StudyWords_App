package com.example.studywords.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studywords.R
import com.example.studywords.databinding.ActivityPostDetailBinding
import java.text.SimpleDateFormat
import java.util.Date

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var viewModel: PostDetailViewModel
    private lateinit var adapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra("postId") ?: return
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PostDetailViewModel(postId) as T
            }
        })[PostDetailViewModel::class.java]



        adapter = CommentAdapter()
        binding.recyclerComments.layoutManager = LinearLayoutManager(this)
        binding.recyclerComments.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        viewModel.postLiveData.observe(this) { post ->
            binding.textTitle.text = post.title
            binding.textContent.text = post.content
            binding.textMeta.text = "${SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(post.timestamp))} / ${post.author}"
        }

        viewModel.commentList.observe(this) {
            adapter.submitList(it)
        }

        binding.buttonSendComment.setOnClickListener {
            val content = binding.editComment.text.toString()
            if (content.isNotBlank()) {
                viewModel.addComment(content)
                binding.editComment.text.clear()
            }
        }
    }
}