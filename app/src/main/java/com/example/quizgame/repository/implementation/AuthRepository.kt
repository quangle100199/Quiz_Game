package com.example.quizgame.repository.implementation

import android.util.Log
import com.example.quizgame.repository.AuthRepositor
import com.example.quizgame.repository.GoogleAuthRepositor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository : AuthRepositor {
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun signUp(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    onComplete(user)
                } else {
                    onError("Authentication failed")
                }
            }
    }

    override fun signIn(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    onComplete(user)
                } else {
                    onError("Authentication failed")
                }
            }
    }

    override fun signOut(
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.signOut()
        val user = firebaseAuth.currentUser
        onComplete(user)
    }

    override fun sendEmailToResetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SendEmailResetPassword", "Email sent.")
                }
            }
    }
}

class GoogleAuthRepository: GoogleAuthRepositor {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signInWithGoogle(idToken : String , onComplete : (Boolean) -> Unit){
        //lấy account trong gg aut dựa trên idToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}