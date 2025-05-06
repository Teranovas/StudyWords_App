package com.example.studywords.community

data class Comment(

    val author: String = "익명",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)