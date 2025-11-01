package com.sample.tidbooktimer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sample.tidbooktimer.data.model.OrganizationDataModel
import com.sample.tidbooktimer.data.model.OrganizationDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    fun storeOrgDetail(orgIds: List<String?>, orgNames: List<String?>) {
        viewModelScope.launch {
            _uiState.value = storeOrgDetailBackground(orgIds, orgNames)
        }
    }

    suspend fun storeOrgDetailBackground(
        orgIds: List<String?>, // Pass up to 4 org IDs
        orgNames: List<String?> // Corresponding names
    ): StoreOrgDetailState {
        return try {
            val uid = fireBaseAuth.currentUser?.uid
                ?: return StoreOrgDetailState.Error("User not authenticated")

            // Validate at least one orgId
            if (orgIds.isEmpty() || orgNames.isEmpty()) {
                return StoreOrgDetailState.Error("At least one organization is required")
            }

            // Build orgDetails map dynamically
            val orgDetailsMap = mutableMapOf<String, OrganizationDataModel>()
            for (i in orgIds.indices) {
                val orgId = orgIds[i]?.trim()
                // Skip if null or blank
                if (orgId.isNullOrBlank()) continue
                val orgName = orgNames.getOrNull(i)?.trim().orEmpty()
                orgDetailsMap[orgId] = OrganizationDataModel(
                    orgId = orgId,
                    orgName = orgName,
                )
            }

            val orgData = OrganizationDetailModel(
                orgDetails = orgDetailsMap,
            )
            fireStore
                .collection("users")
                .document(uid)
                .set(orgData, SetOptions.merge())
                .await()
            StoreOrgDetailState.Success(SuccessPassBackValue(orgIds = orgIds.filterNotNull()))
        } catch (e: Exception) {
            StoreOrgDetailState.Error(e.message ?: "Unknown error")
        }
    }
}

data class SuccessPassBackValue(val orgIds: List<String>)

sealed class StoreOrgDetailState() {
    object Idle : StoreOrgDetailState()
    object Loading : StoreOrgDetailState()
    data class Success(val successValue: SuccessPassBackValue) : StoreOrgDetailState()
    data class Error(val message: String) : StoreOrgDetailState()
}