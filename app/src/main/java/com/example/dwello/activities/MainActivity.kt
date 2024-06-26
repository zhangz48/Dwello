package com.example.dwello.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dwello.ui.theme.DwelloTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.dwello.R

val customFontFamily = FontFamily(
    Font(R.font.cormorant_garamond_regular),
    Font(R.font.cormorant_garamond_bold, FontWeight.Bold),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium),
    // Add other font variants as needed
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DwelloTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        bottomNavigationScreens.find { it.route == currentDestination?.route } ?: Screen.Home
    val isLoggedIn = checkIfLoggedIn()

    Scaffold(
        bottomBar = {
            NavigationBar(
                allScreens = bottomNavigationScreens,
//                onTabSelected = { newScreen -> navController.navigateSingleTopTo(newScreen.route)
//                },
                onTabSelected = { newScreen ->
                    if (isLoggedIn || newScreen == Screen.Home) {
                        navController.navigateSingleTopTo(newScreen.route)
                    } else {
                        navController.navigateSingleTopTo(AuthScreen.Auth.route)
                    }
                },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            isLoggedIn = isLoggedIn,
        )
    }
}

private fun checkIfLoggedIn(): Boolean {
    // Implement your logic to check if the user is logged in
    // This could be checking a token in shared preferences or a cached user object
    return false // Replace with actual logic
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DwelloTheme {
        MainScreen()
    }
}