package com.example.dwello.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dwello.ui.theme.Red100
import com.example.dwello.viewmodel.AuthViewModel
import com.example.dwello.viewmodel.SignInState
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    Log.d("SignUpScreen", "SignUpScreen Composable rendered")

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isEntered = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
    val isButtonEnabled = isEntered && password == confirmPassword && emailError == null

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authViewModel.signInState.collectAsState().value) {
        val signInState = authViewModel.signInState.value
        if (signInState is SignInState.Failure) {
            Toast.makeText(context, signInState.errorMessage, Toast.LENGTH_SHORT).show()
            authViewModel.resetSignInState()
        } else if (signInState is SignInState.Success) {
            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
            authViewModel.resetSignInState()
            onSignUpSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Red100
                )
            }
        }
        Text(
            "Find Your Perfect Home with a Swipe.",
            fontSize = 24.sp,
            color = Red100,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Join Dwello now. It's free!",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                    null
                } else {
                    "Invalid email address"
                }
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError != null) {
            Text(
                text = emailError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                }
            }
        )

        if (password != confirmPassword && confirmPassword.isNotEmpty()) {
            errorMessage = "Passwords do not match"
        } else {
            errorMessage = null
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isButtonEnabled) {
                    coroutineScope.launch {
                        authViewModel.signUp(email, password, firstName, lastName) {
                            // This will be called after sign up success
                            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            onSignUpSuccess()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(48.dp),
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC91818),
                disabledContainerColor = Color(0xFFD26971)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "SIGN UP",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
