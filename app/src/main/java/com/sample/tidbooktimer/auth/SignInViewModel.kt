package com.sample.tidbooktimer.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    private val _signInUiState = MutableStateFlow("")
    val uiState = _signInUiState.asStateFlow()

    fun signIn(email: String, password: String) {
        // Implement sign-in logic here, e.g., using FirebaseAuth or your backend API
        // Update _signInUiState based on the result (loading, success, error)
    }

}

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Error(val message: String) : SignInState()
    object Success : SignInState()
}