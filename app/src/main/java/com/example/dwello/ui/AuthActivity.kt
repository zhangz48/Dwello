package com.example.dwello.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dwello.activities.customFontFamily
import com.example.dwello.viewmodel.AuthViewModel
import com.example.dwello.viewmodel.SignInState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


@Composable
fun AuthScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: (email: String, password: String) -> Unit,
    authViewModel: AuthViewModel,
    selectedTab: String? = null
) {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    val isEntered = email.text.isNotBlank() && password.text.isNotBlank() && emailError == null
    val signInState by authViewModel.signInState.collectAsState()
    val context = LocalContext.current

    Log.d("AuthScreen", "AuthScreen Composable rendered")

    LaunchedEffect(signInState) {
        if (signInState is SignInState.Failure) {
            Toast.makeText(context, (signInState as SignInState.Failure).errorMessage, Toast.LENGTH_SHORT).show()
            authViewModel.resetSignInState() // Reset the sign-in state
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Dwello", style = TextStyle(
                fontFamily = customFontFamily, fontSize = 48.sp, fontWeight = FontWeight.Medium
            ), modifier = Modifier.padding(bottom = 50.dp), textAlign = TextAlign.Center
        )

        CustomTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (android.util.Patterns.EMAIL_ADDRESS.matcher(it.text).matches()) {
                    null
                } else {
                    "Invalid email address"
                }
            },
            label = "Email",
            keyboardType = KeyboardType.Email, // Ensure this is KeyboardType.Email
            isError = emailError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp)
        )

        if (emailError != null) {
            Text(
                text = emailError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            keyboardType = if (passwordVisible) KeyboardType.Text else KeyboardType.Password,
            isPassword = true,
            passwordVisible = passwordVisible,
            onVisibilityChange = { passwordVisible = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = {
                /* Add password recovery logic here */
                Log.d("AuthScreen", "Forgot password clicked")
            }) {
                Text(
                    text = "Forgot password?",
                    color = Color(0xFF3592FF),
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                /* Add authentication logic here */
                Log.d("AuthScreen", "Log In clicked")
                onSignInClick(email.text, password.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = isEntered,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC91818), disabledContainerColor = Color(0xFFD26971)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                "Log In", color = Color.White, style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        Spacer(modifier = Modifier.height(130.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ", color = Color.Gray
            )
            ClickableText(
                text = AnnotatedString("Sign Up"), onClick = {
                    onSignUpClick()
                    Log.d("AuthScreen", "Sign Up clicked") // Add debug log
                }, style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3592FF), textDecoration = TextDecoration.Underline
                )
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onVisibilityChange: ((Boolean) -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.border(
            BorderStroke(
                1.dp, if (isError) MaterialTheme.colorScheme.error else Color(0xFFC6C6C6)
            ), shape = RoundedCornerShape(4.dp)
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F0F0),
            unfocusedContainerColor = Color(0xFFF1F0F0),
            disabledContainerColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error
        ),
        shape = RectangleShape,
        isError = isError,
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                IconButton(onClick = { onVisibilityChange?.invoke(!passwordVisible) }) {
                    Icon(
                        imageVector = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        },
        visualTransformation = (
                if (isPassword && !passwordVisible) PasswordVisualTransformation()
                else VisualTransformation.None
                )
    )
}