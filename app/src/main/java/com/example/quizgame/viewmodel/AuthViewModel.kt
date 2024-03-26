package com.example.quizgame.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgame.repository.implementation.AuthRepository
import com.example.quizgame.repository.implementation.GoogleAuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application) : AndroidViewModel(application) { // chứa ngữ cảnh của ứng dụng
    private var repository = AuthRepository()

    private var _currentUserLiveData = MutableLiveData<FirebaseUser>()
    val currentUserLiveData: LiveData<FirebaseUser?> = _currentUserLiveData

    private var _errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessageLiveData: LiveData<String> = _errorMessageLiveData

    init {
        Log.d("VMcheck", "VM created")
    }

    override fun onCleared() {
        Log.d("VMcheck", "VM cleared")

        super.onCleared()
    }

    fun signUp(email: String, password: String) {
        repository.signUp(
            email,
            password,
            onComplete = { user ->
                _currentUserLiveData.postValue(user)
            },
            onError = { errorMessage ->
                _errorMessageLiveData.postValue(errorMessage)
            }
        )
    }

    fun signIn(email: String, password: String) {
        repository.signIn(
            email,
            password,
            onComplete = { user ->
                _currentUserLiveData.postValue(user)
            },
            onError = { errorMessage ->
                _errorMessageLiveData.postValue(errorMessage)
            }
        )
    }

    fun signOut() {
        repository.signOut(
            onComplete = { user ->
                _currentUserLiveData.postValue(user)
            },
            onError = { errorMessage ->
                _errorMessageLiveData.postValue(errorMessage)
            }
        )
    }

    fun sendEmailToResetPassword(email: String) {
        repository.sendEmailToResetPassword(email)
    }
}

class GoogleAuthViewModel : ViewModel(){

    private val _signInSuccessLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val signInSuccessLiveData : LiveData<Boolean> get() = _signInSuccessLiveData
    private val googleAuthRepository : GoogleAuthRepository = GoogleAuthRepository()

    fun signInWithGoogle(idToken : String){
        googleAuthRepository.signInWithGoogle(idToken) { isSuccess ->
            _signInSuccessLiveData.postValue(isSuccess)
        }
    }
}