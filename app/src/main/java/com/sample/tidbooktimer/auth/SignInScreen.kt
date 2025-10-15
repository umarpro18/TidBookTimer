package com.sample.tidbooktimer.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun SignInScreen() {
    val viewModel: SignInViewModel = hiltViewModel<SignInViewModel>()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Text("Sign In Screen", color = androidx.compose.ui.graphics.Color.Black, fontSize = 32.sp)
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}