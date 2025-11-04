package com.sample.tidbooktimer.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val fireBaseAuth: FirebaseAuth,
    val fireStore: FirebaseFirestore
) : ViewModel() {

    private val _signInUiState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInUiState = _signInUiState.asStateFlow()

    private val _navigationSignInEvent = MutableSharedFlow<NavigationSignInEvent>(1)
    val navigationSignInEvent = _navigationSignInEvent.asSharedFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInUiState.value = SignInState.Loading
            fireBaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        // On success
                        result.result.user?.let {
                            _signInUiState.value = SignInState.Success

                            val docRef = fireStore.collection("users").document(it.uid)
                            docRef.get().addOnSuccessListener { document ->
                                if (document.exists()) {
                                    if (document.contains("orgDetails")) {
                                        val orgDetails = document.get("orgDetails") as? Map<*, *>
                                        if (!orgDetails.isNullOrEmpty()) {
                                            // orgDetails exists and has data
                                            val orgIds: List<String> =
                                                orgDetails.keys.mapNotNull { key ->
                                                    key.toString()
                                                }
                                            _navigationSignInEvent.tryEmit(
                                                NavigationSignInEvent.NavigateToSelectOrgScreen(
                                                    orgIds
                                                )
                                            )
                                        } else {
                                            // orgDetails is empty
                                            _navigationSignInEvent.tryEmit(
                                                NavigationSignInEvent.NavigateToStoreOrgScreen
                                            )
                                        }
                                    } else {
                                        // orgDetails field does not exist
                                        _navigationSignInEvent.tryEmit(
                                            NavigationSignInEvent.NavigateToStoreOrgScreen
                                        )
                                    }
                                } else {
                                    // Document does not exist
                                }
                            }.addOnFailureListener { e ->
                                _signInUiState.value = SignInState.Error("Authentication failed")
                            }
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

}

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    data class Error(val message: String) : SignInState()
    object Success : SignInState()
}

sealed class NavigationSignInEvent {
    data class NavigateToSelectOrgScreen(val orgIds: List<String>) : NavigationSignInEvent()
    object NavigateToStoreOrgScreen : NavigationSignInEvent()
}