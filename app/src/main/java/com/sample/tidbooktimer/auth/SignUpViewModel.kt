package com.sample.tidbooktimer.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        _uiState.value = SignUpUiState.Loading
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            _uiState.value = SignUpUiState.Success
                        } else {
                            _uiState.value = SignUpUiState.Error(
                                updateTask.exception?.message ?: "Failed to update profile"
                            )
                        }
                    }
                } else {
                    _uiState.value =
                        SignUpUiState.Error(task.exception?.message ?: "Sign-up failed")
                }
            }
    }
}

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
    object Success : SignUpUiState()
}