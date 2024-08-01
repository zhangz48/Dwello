package com.example.dwello.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dwello.viewmodel.AuthViewModel
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.dwello.utils.logBackStack
import com.example.dwello.viewmodel.PropertyViewModel
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController,
    propertyViewModel: PropertyViewModel
) {

    Log.d("AccountScreen", "AccountScreen Composable rendered")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBarAccount()
        Spacer(modifier = Modifier.height(16.dp))

        AccountItem("Personal Information")

        AccountItem("Sign In & Security")

        AccountItem("About")

        ClearCacheButton(propertyViewModel)

        Spacer(modifier = Modifier.weight(1f))
        SignOutButton(authViewModel, navController)
    }
    Log.d("AccountScreen", "Account screen called")
}

@Composable
fun TopBarAccount() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My account",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }
}

@Composable
fun AccountItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle item click */ }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun ClearCacheButton(propertyViewModel: PropertyViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    propertyViewModel.clearLocalDatabase()
                    Toast.makeText(context, "Cache cleared", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(
                text = "Clear Cache",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
fun SignOutButton(authViewModel: AuthViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = {
                authViewModel.signOut()
                navController.navigate(com.example.dwello.activities.AuthScreen.Auth.route) {
//                    popUpTo(navController.graph.startDestinationId) {
//                        saveState = true
//                        inclusive = true
//                    }
                }
                navController.logBackStack() // Log the back stack after navigation
            }
        ) {
            Text(
                text = "Sign out",
                style = TextStyle(
                    color = Color(0xFFC91818),
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
        }
    }
}