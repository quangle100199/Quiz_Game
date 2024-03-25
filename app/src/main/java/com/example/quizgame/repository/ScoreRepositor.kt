package com.example.quizgame.repository

import com.example.quizgame.data.Score

interface ScoreRepositor {
    fun sendScore(score : Score)
}