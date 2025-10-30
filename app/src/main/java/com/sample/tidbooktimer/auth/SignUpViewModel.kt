package com.sample.tidbooktimer.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.tidbooktimer.data.model.UserDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firebaseStore: FirebaseFirestore
) : ViewModel() {

    private val _personalNumber = MutableStateFlow("")
    val personalNumber = _personalNumber.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isPersonalNumberFieldError = MutableStateFlow(false)
    val isPersonalNumberFieldError = _isPersonalNumberFieldError.asStateFlow()

    private val _isEmailFieldError = MutableStateFlow(false)
    val isEmailFieldError = _isEmailFieldError.asStateFlow()

    private val _isPasswordFieldError = MutableStateFlow(false)
    val isPasswordFieldError = _isPasswordFieldError.asStateFlow()

    fun setPersonalNumber(value: String) {
        _personalNumber.value = value
    }

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun signUp(
        personalNumber: String,
        email: String,
        password: String
    ) {
        _uiState.value = SignUpUiState.Loading
        // Validate the input fields locally
        _isPersonalNumberFieldError.value = !isValidPersonalNumber(personalNumber)
        _isEmailFieldError.value = !isValidEmail(email)
        _isPasswordFieldError.value = !isValidPassword(password)
        if (_isPersonalNumberFieldError.value || _isEmailFieldError.value || _isPasswordFieldError.value) {
            _uiState.value = SignUpUiState.Idle
            return
        }

        // Step 1: Check if personal number already exists
        firebaseStore.collection("users")
            .whereEqualTo("personalNumber", personalNumber)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    // Someone already signed up with this personal number
                    _uiState.value =
                        SignUpUiState.Error("Personal number already exists, sign up failed")
                    return@addOnSuccessListener
                }

                // Step 2: Proceed with Firebase Auth signup
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = firebaseAuth.currentUser?.uid
                            if (uid == null) {
                                _uiState.value = SignUpUiState.Error("Failed to get user ID")
                                return@addOnCompleteListener
                            }

                            // Step 3: Prepare user data
                            val userData = UserDataModel(
                                personalNumber = personalNumber,
                                email = email,
                                createdAt = Timestamp.now()
                            )

                            // Step 4: Save user data under /users/{uid}
                            firebaseStore.collection("users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    // Navigate to next screen to collect org numbers
                                    _uiState.value = SignUpUiState.Success
                                }
                                .addOnFailureListener { e ->
                                    _uiState.value = SignUpUiState.Error(
                                        "Failed to save user data: ${e.message}"
                                    )
                                }
                        } else {
                            _uiState.value = SignUpUiState.Error(
                                task.exception?.message ?: "Sign-up failed"
                            )
                        }
                    }
            }
            .addOnFailureListener { e ->
                _uiState.value =
                    SignUpUiState.Error("Failed to check personal number: ${e.message}")
            }
    }

    /**
     * Simple email validation helper
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /** Simple password validation: at least 6 characters */
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    /** Simple personal number validation: at least 10 digits */
    private fun isValidPersonalNumber(personalNumber: String): Boolean {
        return personalNumber.matches(Regex("^[0-9]{12}$"))
    }
}

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
    object Success : SignUpUiState()
}