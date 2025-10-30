package com.sample.tidbooktimer.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StoreOrgViewModel @Inject constructor(
    val fireBaseAuth: FirebaseAuth,
    val fireStore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow<StoreOrgDetailState>(StoreOrgDetailState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _orgNo1 = MutableStateFlow("")
    val orgNo1 = _orgNo1.asStateFlow()

    private val _orgNo2 = MutableStateFlow("")
    val orgNo2 = _orgNo2.asStateFlow()

    private val _orgNo3 = MutableStateFlow("")
    val orgNo3 = _orgNo3.asStateFlow()

    private val _orgNo4 = MutableStateFlow("")
    val orgNo4 = _orgNo4.asStateFlow()

    fun setOrgNo1(value: String) {
        _orgNo1.value = value
    }

    fun setOrgNo2(value: String) {
        _orgNo2.value = value
    }

    fun setOrgNo3(value: String) {
        _orgNo3.value = value
    }

    fun setOrgNo4(value: String) {
        _orgNo4.value = value
    }

    fun storeOrgDetail(
        personalNumber: String,
        orgNo1: String? = null,
        orgNo2: String? = null,
        orgNo3: String? = null,
        orgNo4: String? = null,
    ) {
        _uiState.value = StoreOrgDetailState.Loading

        // Validate at least one orgNo
        val orgNumbers = listOf(orgNo1, orgNo2, orgNo3, orgNo4).filterNot { it.isNullOrBlank() }
        if (orgNumbers.isEmpty()) {
            _uiState.value =
                StoreOrgDetailState.Error("At least one organization number is required")
            return
        }

        val uid = fireBaseAuth.currentUser?.uid
        if (uid == null) {
            _uiState.value = StoreOrgDetailState.Error("User not authenticated")
            return
        }

        val userDocRef = fireStore.collection("users").document(uid)

        val orgData = mapOf(
            "orgDetails" to mapOf(
                "orgNo1" to orgNo1,
                "orgNo2" to orgNo2,
                "orgNo3" to orgNo3,
                "orgNo4" to orgNo4
            ),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        userDocRef.set(orgData, SetOptions.merge())
            .addOnSuccessListener {
                _uiState.value = StoreOrgDetailState.Success
            }
            .addOnFailureListener { e ->
                _uiState.value = StoreOrgDetailState.Error(e.message ?: "Unknown error")
            }
    }
}

sealed class StoreOrgDetailState() {
    object Idle : StoreOrgDetailState()
    object Loading : StoreOrgDetailState()
    object Success : StoreOrgDetailState()
    data class Error(val message: String) : StoreOrgDetailState()
}