package com.sample.tidbooktimer.tidbookhome

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    val auth: FirebaseAuth,
    val dataStore: DataStore<Preferences>
) {
    suspend fun logout() {
        auth.signOut()
        dataStore.edit { it.clear() }
    }

    fun currentUserId(): String? = auth.currentUser?.uid
}