package com.example.quizgame.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgame.R
import com.example.quizgame.databinding.FragmentForgotPasswordBinding
import com.example.quizgame.viewmodel.AuthViewModel

class ForgotPasswordFragment : Fragment() {
    private lateinit var mFragmentForgotPasswordBinding : FragmentForgotPasswordBinding
    private val viewModel : AuthViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentForgotPasswordBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mFragmentForgotPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        mFragmentForgotPasswordBinding.btnSend.setOnClickListener {
            val email = mFragmentForgotPasswordBinding.etEmailSendPassword.text.toString()
            viewModel.sendEmailToResetPassword(email)
            navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }
}