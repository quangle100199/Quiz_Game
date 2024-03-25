package com.example.quizgame.repository

import com.example.quizgame.data.Question

interface QuestionRepositor {
    fun getQuestionNum(): Int?
    fun addQuestion(question: Question)
    fun getQuestions(onQuestionLoad: (MutableList<Question>?) -> Unit)


}