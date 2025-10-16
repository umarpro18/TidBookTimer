package com.sample.tidbooktimer.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _signInUiState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInUiState = _signInUiState.asStateFlow()

    fun signIn(email: String, password: String) {
        _signInUiState.value = SignInState.Loading
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    // On success
                    result.result.user?.let {
                        _signInUiState.value = SignInState.Success
                        return@addOnCompleteListener
                    }
                    _signInUiState.value = SignInState.Error("Authentication failed")
                } else {
                    // On error
                    _signInUiState.value = SignInState.Error("Authentication failed")
                }
            }
    }

}

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Error(val message: String) : SignInState()
    object Success : SignInState()
}