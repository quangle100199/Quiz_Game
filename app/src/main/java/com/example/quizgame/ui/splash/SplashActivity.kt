package com.example.quizgame.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.ui.auth.AuthNavigationActivity
import com.example.quizgame.ui.home.HomeNavigationActivity
import com.example.quizgame.viewmodel.AuthViewModel

class SplashActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        Handler().postDelayed({
            if (viewModel.currentUserLiveData != null) {
                startActivity(Intent(this, HomeNavigationActivity::class.java))
            } else {
                startActivity(Intent(this, AuthNavigationActivity::class.java))
            }
            finish()
        }, 3000)

    }
}