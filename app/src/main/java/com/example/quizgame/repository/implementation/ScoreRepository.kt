package com.example.quizgame.repository.implementation

import android.util.Log
import com.example.quizgame.data.Score
import com.example.quizgame.repository.ScoreRepositor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScoreRepository : ScoreRepositor {
    private val db : FirebaseFirestore = Firebase.firestore

    // send data to firebase
    override fun sendScore(score : Score){
        val _score = hashMapOf(
            "correct" to score.correctNum,
            "wrong" to score.incorrectNum
        )
        db.collection("scores").document("Kết quả")
            .set(_score)
            .addOnSuccessListener { Log.d("sendScore", "successfully!") }
            .addOnFailureListener { e -> Log.w("sendScore", "Error", e) }
    }
}