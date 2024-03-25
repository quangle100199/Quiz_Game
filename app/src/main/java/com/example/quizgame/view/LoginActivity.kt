package com.example.quizgame.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityLoginBinding
import com.example.quizgame.model.Question
import com.example.quizgame.repository.QuestionRepository
import com.example.quizgame.viewmodel.AuthViewModel
import com.example.quizgame.viewmodel.GoogleAuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var mLoginBinding : ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var googleAuthViewModel: GoogleAuthViewModel

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    val RC_SIGN_IN = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mLoginBinding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        googleAuthViewModel = ViewModelProvider(this)[GoogleAuthViewModel::class.java]

        mLoginBinding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        mLoginBinding.signInButton.setOnClickListener{
            val email = mLoginBinding.txtEmailIn.getText().toString()
            val pass : String = mLoginBinding.txtPasswordIn.getText().toString()
            if (!email.isEmpty() && !pass.isEmpty()){
                viewModel.signIn(email, pass)
                viewModel.currentUserLiveData.observe(this) { user ->
                    if (user != null) {
                        // Navigate to the desired destination upon successful login
                        startActivity(Intent(this, HomePageActivity::class.java))
                        Toast.makeText(this, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(this, "Your Email or Password is not correct", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý sign in với Google
        mLoginBinding.btnContinueWithGoogle.setOnClickListener {
            Log.e("signInGoogleButton", "signInGoogleButton")
            startSignInWithGoogle()
        }

        googleAuthViewModel.signInSuccessLiveData.observe(this){isSuccess ->
            if (isSuccess){
                startActivity(Intent(this, HomePageActivity::class.java))
                Toast.makeText(this, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.e("GoogleTest" , "Login fail")
            }
        }

        // Xử lý forgot password
        mLoginBinding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }

    //Tạo intent để thực hiện đăng nhập Google qua GoogleSignIn.getClient()
    private fun startSignInWithGoogle() {
        val signInIntent = getGoogleSignInIntent() // lấy account từ ggauth và xác thực
        Log.e("signInGoogleButton", "$signInIntent")
        startActivityForResult(signInIntent, RC_SIGN_IN) //request code đại diện cho intent// gửi đi request và chờ đợi kq

    }

    //Tạo và trả về một Intent để đăng nhập google
    //GoogleSignInOptions.Builder xây dựng tùy chọn đăng nhập (cái activity của google hiện lên khi bấm button)
    //requestIdToken: yêu cầu token ID từ máy chủ của bạn thông qua web client ID
    private fun getGoogleSignInIntent() : Intent {
        Log.e("signInGoogleButton", "getGoogleSignInIntent")
        // dựng lên cửa sổ đăng nhập mặc định của gg
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id)).requestEmail().build()
        Log.e("signInGoogleButton", "gso: ${gso.account}")

        val googleSignInClient = GoogleSignIn.getClient(
            this, gso // hiện lên fragment
        )
        Log.e("signInGoogleButton", "$googleSignInClient")

        return googleSignInClient.signInIntent
    }

    //Khi một hoạt động con (hoạt động từ dịch vụ bên ngoài) hoàn thành và trả về kết quả, gọi onActivityResult để xử lý kết quả đó
    //Nhận kết quả từ cuộc gọi startActivityForResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                val idToken = handleGoogleSignInResult(data)
                Log.e("signInGoogleButton", "idToken : $idToken")

                idToken?.let {
                    googleAuthViewModel.signInWithGoogle(it)
                }
            } else {
                Log.d("GoogleTest" , "Data is null")
            }
        }

    }

    //Xử lý kết quả trả về từ hành động đăng nhập ggl
    //Gọi GoogleSignIn.getSignedInAccountFromIntent(data) để lấy thông tin tài khoản sau khi đăng nhập thành công
    //Trả về idToken
    private fun handleGoogleSignInResult(data : Intent?) : String? {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            return account?.idToken
        } catch (e: ApiException){
            // Xử lý lỗi nếu cần thiết
            Log.e("GoogleTest" , "ApiException: ${e.message}")
        }
        return null
    }

}