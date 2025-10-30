package com.sample.tidbooktimer.data.model

import com.google.firebase.Timestamp

data class UserDataModel(
    val uid: String = "",
    val personalNumber: String = "",
    val email: String = "",
    val phoneNumber: String? = null, // future impl
    val createdAt: Timestamp? = null
)