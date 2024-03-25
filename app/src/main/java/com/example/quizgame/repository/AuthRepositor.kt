package com.example.quizgame.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepositor {
    fun signUp(email: String,
               password: String,
               onComplete: (FirebaseUser?) -> Unit,
               onError: (String) -> Unit)

    fun signIn(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    )

    fun signOut(
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    )

    fun sendEmailToResetPassword(email: String)
}

interface GoogleAuthRepositor{
    fun signInWithGoogle(idToken : String , onComplete : (Boolean) -> Unit)
}