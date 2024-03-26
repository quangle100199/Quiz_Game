package com.example.quizgame.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgame.R
import com.example.quizgame.databinding.FragmentLoginBinding
import com.example.quizgame.viewmodel.AuthViewModel
import com.example.quizgame.viewmodel.GoogleAuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment() {
    private lateinit var mFragmentLoginBinding : FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private val googleAuthViewModel: GoogleAuthViewModel by viewModels()
    private lateinit var navController: NavController

    val RC_SIGN_IN = 40

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
//        googleAuthViewModel = ViewModelProvider(this)[GoogleAuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mFragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        mFragmentLoginBinding.btnSignUp.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        mFragmentLoginBinding.signInButton.setOnClickListener{
            val email = mFragmentLoginBinding.txtEmailIn.getText().toString()
            val pass : String = mFragmentLoginBinding.txtPasswordIn.getText().toString()
            if (!email.isEmpty() && !pass.isEmpty()){
                viewModel.signIn(email, pass)
                viewModel.apply {
                    currentUserLiveData.apply {
                        observe(viewLifecycleOwner) {
                            if (it != null) {
                                navController.navigate(R.id.action_loginFragment_to_homeNavigationActivity)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Please Enter Email and Pass", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý sign in với Google
        mFragmentLoginBinding.btnContinueWithGoogle.setOnClickListener {
            Log.e("signInGoogleButton", "signInGoogleButton")
            startSignInWithGoogle()
        }

        googleAuthViewModel.signInSuccessLiveData.observe(viewLifecycleOwner){isSuccess ->
            if (isSuccess){
                navController.navigate(R.id.homepageFragment)
            } else {
                Log.e("GoogleTest" , "Login fail")
            }
        }

        // Xử lý forgot password
        mFragmentLoginBinding.tvForgotPassword.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
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
            requireActivity(), gso // hiện lên fragment
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