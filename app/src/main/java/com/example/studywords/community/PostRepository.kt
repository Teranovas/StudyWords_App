package com.example.studywords.community

import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class PostRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsRef = db.collection("posts")

    fun getAllPosts(): LiveData<List<Post>> {
        val liveData = MutableLiveData<List<Post>>()

        postsRef.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }

                val postList = snapshot.documents.mapNotNull { it.toObject(Post::class.java) }
                liveData.value = postList
            }

        return liveData
    }

    fun addPost(post: Post) {
        val docRef = postsRef.document(post.id.ifBlank { postsRef.document().id })
        val postWithId = post.copy(id = docRef.id)
        docRef.set(postWithId)
    }

    fun deletePost(id: String) {
        postsRef.document(id).delete()
    }
}
