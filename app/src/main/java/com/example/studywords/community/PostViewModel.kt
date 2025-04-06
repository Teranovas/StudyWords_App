package com.example.studywords.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val repository = PostRepository()

    val postList: LiveData<List<Post>> = repository.getAllPosts()

    fun addPost(title: String, content: String, author: String) {
        val post = Post(
            title = title,
            content = content,
            author = author
        )
        repository.addPost(post)
    }

    fun deletePost(postId: String) {
        repository.deletePost(postId)
    }
}
