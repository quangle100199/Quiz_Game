package com.example.quizgame.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityQuestionBinding
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.example.quizgame.viewmodel.AuthViewModel
import com.example.quizgame.viewmodel.QuestionViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var mSignUpBinding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mSignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mSignUpBinding.root)

        setSupportActionBar(mSignUpBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        mSignUpBinding.signUpButton.setOnClickListener {
            val email = mSignUpBinding.txtEmailIn.text.toString().trim()
            val pass : String = mSignUpBinding.txtPasswordIn.text.toString().trim()
            if (!email.isEmpty() && !pass.isEmpty()){
                viewModel.signUp(email, pass)
                Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                viewModel.currentUserLiveData.observe(this){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                viewModel.errorMessageLiveData.observe(this){
                    errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}