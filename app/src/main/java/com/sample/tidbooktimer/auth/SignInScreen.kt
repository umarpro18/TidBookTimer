package com.sample.tidbooktimer.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.tidbooktimer.R


@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onNavigateToSignUp: () -> Unit,
    onSignInSuccess: () -> Unit,
    navigateToStoreOrgScreen: () -> Unit,
    navigateToSelectOrgScreen: (orgIds: List<String>) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    val uiState = viewModel.signInUiState.collectAsStateWithLifecycle()
    val isLoading = uiState.value == SignInState.Loading

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is SignInState.Error -> {
                android.widget.Toast.makeText(
                    context,
                    (uiState.value as SignInState.Error).message,
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }

            is SignInState.Success -> {
                //onSignInSuccess()
            }

            SignInState.Idle, SignInState.Loading -> {
                // No action needed
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationSignInEvent.collect { event ->
            when (event) {
                is NavigationSignInEvent.NavigateToSelectOrgScreen -> {
                    Log.d("SignInScreen", "NavigateToSelectOrgScreen: ${event.orgIds}")
                    navigateToSelectOrgScreen(event.orgIds)
                }

                is NavigationSignInEvent.NavigateToStoreOrgScreen -> {
                    Log.d("SignInScreen", "NavigateToStoreOrgScreen:")
                    navigateToStoreOrgScreen()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Image(
            painter = androidx.compose.ui.res.painterResource(com.sample.tidbooktimer.R.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, end = 24.dp, top = 120.dp, bottom = 24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.welcome),
                color = androidx.compose.ui.graphics.Color.Magenta,
                fontSize = 40.sp,
                fontWeight = Bold,
            )

            Spacer(modifier = Modifier.size(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { input ->
                    if (!input.contains(" ")) {
                        email = input
                    }
                },
                label = { Text(stringResource(R.string.email)) },
                isError = isEmailError,
                supportingText = {
                    if (isEmailError) {
                        Text(
                            text = stringResource(R.string.invalid_email),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Magenta,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.size(24.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { input ->
                    if (!input.contains(" ")) {
                        password = input
                    }
                },
                label = { Text(stringResource(R.string.password)) },
                isError = isPasswordError,
                supportingText = {
                    if (isPasswordError) {
                        Text(
                            text = stringResource(R.string.invalid_password),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Magenta,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.size(48.dp))

            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.Magenta
                )
            } else {
                Button(
                    onClick = {
                        isEmailError = !isValidEmail(email)
                        isPasswordError = !isValidPassword(password)
                        if (!isEmailError && !isPasswordError) {
                            viewModel.signIn(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Magenta.copy(alpha = 0.85f),
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.login), fontSize = 18.sp, fontWeight = Bold)
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            TextButton(onClick = { onNavigateToSignUp() }) {
                Text(
                    text = stringResource(R.string.no_account_signup),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
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

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        viewModel = hiltViewModel(),
        onNavigateToSignUp = {}, onSignInSuccess = {}, navigateToStoreOrgScreen = {}, navigateToSelectOrgScreen = {}
    )
}