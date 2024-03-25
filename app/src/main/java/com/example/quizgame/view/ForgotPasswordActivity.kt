package com.example.quizgame.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.example.quizgame.databinding.ActivityResultBinding
import com.example.quizgame.viewmodel.AuthViewModel
import com.example.quizgame.viewmodel.ScoreViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var mForgotPasswordBinding : ActivityForgotPasswordBinding
    private lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_password)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        mForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(mForgotPasswordBinding.root)

        setSupportActionBar(mForgotPasswordBinding.toolbar)

        mForgotPasswordBinding.btnSend.setOnClickListener {
            val email = mForgotPasswordBinding.etEmailSendPassword.text.toString()
            viewModel.sendEmailToResetPassword(email)
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}