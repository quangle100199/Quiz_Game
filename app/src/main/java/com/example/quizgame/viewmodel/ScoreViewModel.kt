package com.example.quizgame.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizgame.data.Score
import com.example.quizgame.repository.implementation.ScoreRepository

class ScoreViewModel : ViewModel() {
    private var repository : ScoreRepository = ScoreRepository()

    fun sendScore(score : Score){
        repository.sendScore(score)
    }
}