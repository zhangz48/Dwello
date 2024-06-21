package com.example.dwello.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.dwello.DwelloApplication
import com.example.dwello.ui.theme.DwelloTheme
import com.example.dwello.viewmodel.UserViewModel
import com.example.dwello.viewmodel.UserViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dwello.data.User

class SignUpActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as DwelloApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DwelloTheme {
                SignUpScreen( viewModel = userViewModel, onBackClick = { finish() })
            }
        }
    }
}

@Composable
fun SignUpScreen(
    viewModel: UserViewModel,
    onBackClick: () -> Unit,
//    navController: NavHostController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }  // To display error messages.

    val isEntered = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank()
    val isButtonEnabled = isEntered && password == confirmPassword

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
                modifier = Modifier.align(Alignment.CenterStart))
            {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Red)
            }
        }
        Text(
            "Find Your Perfect Home with a Swipe.",
            fontSize = 24.sp,
            color = Color.Red,
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
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isButtonEnabled) {
                    viewModel.insert(
                        User(firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password))
//                    navController.popBackStack()
                } else {
                    // Handle password mismatch
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
            Text("SIGN UP",
                color = Color.White,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    DwelloTheme {
//        SignUpScreen( onBackClick = {} )
//    }
//}

// Create a mock UserViewModel for the preview
//class MockUserViewModel : UserViewModel(
//    UserRepository(
//        UserDatabase.getDatabase(null, CoroutineScope(SupervisorJob() + Dispatchers.Main)).userDao()
//    ), CoroutineScope(SupervisorJob() + Dispatchers.Main)
//) {
//    override fun insert(user: User) {
//        // Mock implementation
//    }
//}