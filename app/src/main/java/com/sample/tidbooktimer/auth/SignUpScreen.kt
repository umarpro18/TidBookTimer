package com.sample.tidbooktimer.auth

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.tidbooktimer.R

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpSuccess: () -> Unit,
    onSignInClicked: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val personalNumber = viewModel.personalNumber.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val isEmailFieldError = viewModel.isEmailFieldError.collectAsStateWithLifecycle()
    val isPasswordFieldError = viewModel.isPasswordFieldError.collectAsStateWithLifecycle()
    val isPersonalNumberFieldError =
        viewModel.isPersonalNumberFieldError.collectAsStateWithLifecycle()

    val setPersonalNumber: (String) -> Unit = { viewModel.setPersonalNumber(it) }
    val setEmail: (String) -> Unit = { viewModel.setEmail(it) }
    val setPassword: (String) -> Unit = { viewModel.setPassword(it) }

    val onSignUpClicked: (String, String, String) -> Unit =
        { personalNumber, email, password -> viewModel.signUp(personalNumber, email, password) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState.value == SignUpUiState.Loading

    LaunchedEffect(uiState.value) {
        when (uiState.value) {
            is SignUpUiState.Success -> {
                onSignUpSuccess()
            }

            is SignUpUiState.Error -> {
                val errorMessage = (uiState.value as SignUpUiState.Error).message
                android.widget.Toast.makeText(
                    context,
                    errorMessage,
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    SignUpScreenContent(
        onSignUpClicked,
        onSignInClicked,
        isLoading,
        personalNumber.value,
        email.value,
        password.value,
        isEmailFieldError.value,
        isPasswordFieldError.value,
        isPersonalNumberFieldError.value,
        setPersonalNumber,
        setEmail,
        setPassword,
    )
}

@Composable
fun SignUpScreenContent(
    onSignUpClicked: (String, String, String) -> Unit,
    onSignInClicked: () -> Unit,
    isLoading: Boolean,
    personalNumber: String,
    email: String,
    password: String,
    isEmailError: Boolean,
    isPasswordError: Boolean,
    isPersonalNumberError: Boolean,
    setPersonalNumber: (String) -> Unit,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
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
                .padding(24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(80.dp))
            Text(
                stringResource(R.string.signup),
                color = Color.Magenta,
                fontSize = 40.sp,
                fontWeight = Bold,
            )

            Spacer(modifier = Modifier.size(24.dp))

            OutlinedTextField(
                value = personalNumber,
                onValueChange = { setPersonalNumber(it) },
                label = { Text(stringResource(R.string.personal_number)) },
                isError = isPersonalNumberError,
                supportingText = {
                    if (isPersonalNumberError) {
                        Text(
                            text = stringResource(R.string.invalid_personal_number),
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
            )

            Spacer(modifier = Modifier.size(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { input ->
                    if (!input.contains(" ")) {
                        setEmail(input)
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

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { input ->
                    if (!input.contains(" ")) {
                        setPassword(input)
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
                        onSignUpClicked(personalNumber, email, password)
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
                    Text(stringResource(R.string.signup), fontSize = 18.sp, fontWeight = Bold)
                }
            }

            TextButton(onClick = { onSignInClicked() }, modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.already_signup_goto_sign_in),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun SignUpScreenContentPreview() {
    SignUpScreenContent(
        onSignUpClicked = { _, _, _ -> },
        onSignInClicked = {},
        isLoading = false,
        personalNumber = "",
        email = "",
        password = "",
        isEmailError = false,
        isPasswordError = false,
        isPersonalNumberError = false,
        setPersonalNumber = {},
        setEmail = {},
        setPassword = {},
    )
}