package com.example.quizgame.data

data class Question(
    val answer: String = "",
    val option_a: String = "",
    val option_b: String ="",
    val option_c: String="",
    val option_d: String="",
    val questionContent: String=""
)