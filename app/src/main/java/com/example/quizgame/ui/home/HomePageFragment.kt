package com.example.quizgame.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizgame.R
import com.example.quizgame.databinding.FragmentHomePageBinding
import com.example.quizgame.databinding.FragmentLoginBinding
import com.example.quizgame.viewmodel.AuthViewModel

class HomePageFragment : Fragment() {
    private lateinit var mFragmentHomePageBinding: FragmentHomePageBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentHomePageBinding = FragmentHomePageBinding.inflate(inflater, container, false )
        return mFragmentHomePageBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        mFragmentHomePageBinding.startQuiz.setOnClickListener {
            Log.e("Check", "startQuiz")
            navController.navigate(R.id.action_homepageFragment_to_questionFragment)
        }

        mFragmentHomePageBinding.signOut.setOnClickListener{
            authViewModel.signOut()
            Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_homepageFragment_to_authNavigationActivity)
        }
    }
}