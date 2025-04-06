package com.example.studywords.community

data class Post(
    val id : String = "",
    val title : String = "",
    val content : String = "",
    val author : String = "",
    val timestamp: Long = System.currentTimeMillis()
)
