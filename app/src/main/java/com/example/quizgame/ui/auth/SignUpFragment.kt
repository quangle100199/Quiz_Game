package com.example.quizgame.ui.auth

import android.os.Bundle
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
import com.example.quizgame.databinding.FragmentSignUpBinding
import com.example.quizgame.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {
    private lateinit var mFragmentSignUpBinding : FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return mFragmentSignUpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        mFragmentSignUpBinding.signUpButton.setOnClickListener {
            val email = mFragmentSignUpBinding.txtEmailIn.text.toString().trim()
            val pass: String = mFragmentSignUpBinding.txtPasswordIn.text.toString().trim()
            if (!email.isEmpty() && !pass.isEmpty()) {
                viewModel.signUp(email, pass)
                Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT).show()
                viewModel.currentUserLiveData.observe(viewLifecycleOwner) {
                    navController.navigate(R.id.action_signUpFragment_to_loginFragment)
                }
            } else {
                viewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
                    // Display error message if login fails
                    Toast.makeText(requireContext() , errorMessage , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}