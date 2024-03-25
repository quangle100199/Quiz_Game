package com.example.quizgame.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.viewmodel.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        Handler().postDelayed({
            if (viewModel.currentUserLiveData != null) {
                startActivity(Intent(this, HomePageActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1000)

    }
}