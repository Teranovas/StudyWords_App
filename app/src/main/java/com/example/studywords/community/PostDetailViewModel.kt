package com.example.studywords.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PostDetailViewModel(private val postId: String) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val postLiveData = MutableLiveData<Post>()
    val commentList = MutableLiveData<List<Comment>>()

    init {
        db.collection("posts").document(postId)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(Post::class.java)?.let {
                    postLiveData.value = it
                }
            }

        db.collection("posts").document(postId).collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                commentList.value = snapshot?.toObjects(Comment::class.java) ?: emptyList()
            }
    }

    fun addComment(content: String) {
        val comment = Comment("익명", content)
        db.collection("posts").document(postId)
            .collection("comments").add(comment)
    }
}
