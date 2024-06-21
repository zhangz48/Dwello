package com.example.dwello.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.dwello.ui.theme.DwelloTheme

class AccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DwelloTheme {
                AccountScreen()
            }
        }
    }
}

@Composable
fun AccountScreen() {
        // Show the Account Screen content
        Text("Account Screen")
}