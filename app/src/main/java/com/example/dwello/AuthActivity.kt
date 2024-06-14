package com.example.dwello

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dwello.ui.theme.DwelloTheme


class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DwelloTheme {
                AuthScreen()
            }
        }
    }
}

@Composable
fun AuthScreen() {
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    val isEntered = email.text.isNotBlank() && password.text.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dwello",
            style = TextStyle(
                fontFamily = customFontFamily,
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 50.dp),
            textAlign = TextAlign.Center
        )
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
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
            TextButton(
                onClick = { /* Add password recovery logic here */ }
            ) {
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
            onClick = { /* Add authentication logic here */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = isEntered,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC91818),
                disabledContainerColor = Color(0xFFD26971)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Log In",
                color = Color.White,
                style = TextStyle(fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline))
        }
        Spacer(modifier = Modifier.height(130.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Gray
            )
            ClickableText(
                text = AnnotatedString("Sign Up"),
                onClick = { /* Add navigation to sign up screen logic here */ },
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF3592FF),
                    textDecoration = TextDecoration.Underline
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
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(
            //fontFamily = fontResource(id = R.font.custom_font),
            fontSize = 18.sp
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .border(
                BorderStroke(1.dp, Color(0xFFC6C6C6)),
                shape = RoundedCornerShape(4.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F0F0),
            unfocusedContainerColor = Color(0xFFF1F0F0),
            disabledContainerColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RectangleShape
    )
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    DwelloTheme {
        AuthScreen()
    }
}