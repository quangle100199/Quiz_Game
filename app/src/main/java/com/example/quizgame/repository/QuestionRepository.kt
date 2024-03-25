package com.example.quizgame.repository

import android.util.Log
import com.example.quizgame.model.Question
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class QuestionRepository {
    private val firebaseFireStore : FirebaseFirestore = Firebase.firestore
    private var _questionNum : Int? = null
    private lateinit var _listQuestion: MutableList<Question>

    fun getQuestionNum(): Int?{
        return _questionNum
    }

    fun addQuestion(question: Question){
        firebaseFireStore.collection("question").add(question)
        Log.i("QuangLM12", "Check")
    }

    fun getQuestions(onQuestionLoad: (MutableList<Question>?) -> Unit){
        _listQuestion = mutableListOf()
        firebaseFireStore.collection("question").get()
            .addOnSuccessListener { listQuestion ->

                for (question in listQuestion){


                    val answer = question.getString("answer")?: ""
                    val option_a = question.getString("option_a")?: ""
                    val option_b = question.getString("option_b")?: ""
                    val option_c = question.getString("option_c")?: ""
                    val option_d = question.getString("option_d")?: ""
                    val questionContent = question.getString("questionContent")?: ""

                    val _question = Question(answer, option_a, option_b, option_c, option_d, questionContent)
                    _listQuestion.add(_question)
                }
                _questionNum = _listQuestion.size
                onQuestionLoad(_listQuestion)
            }.addOnFailureListener{
                Log.e("QuangLM12", "Error getting data")
            }
    }
}