package com.example.dwello

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dwello.ui.theme.DwelloTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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

    Scaffold(
        bottomBar = {
            NavigationBar(
                allScreens = bottomNavigationScreens,
                onTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = currentScreen
            )
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun FavouritesScreen() {
    // Favourites screen content
    Text("Favourites Screen")
}

@Composable
fun AccountScreen() {
    // Account screen content
    Text("Account Screen")
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DwelloTheme {
        MainScreen()
    }
}