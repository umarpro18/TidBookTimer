package com.sample.tidbooktimer

import androidx.lifecycle.ViewModel
import com.sample.tidbooktimer.tidbookhome.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(sessionManager: SessionManager) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLogged = _isUserLoggedIn.asStateFlow()

    init {
        _isUserLoggedIn.value = sessionManager.currentUserId() != null
    }
}