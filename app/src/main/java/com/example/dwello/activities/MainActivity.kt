package com.example.dwello.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dwello.ui.theme.DwelloTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dwello.R
import com.example.dwello.utils.logBackStack
import com.example.dwello.viewmodel.AuthViewModel
import com.example.dwello.viewmodel.MapsViewModel
import com.example.dwello.viewmodel.MapsViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

val customFontFamily = FontFamily(
    Font(R.font.cormorant_garamond_regular),
    Font(R.font.cormorant_garamond_bold, FontWeight.Bold),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium),
    // Add other font variants as needed
)

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private val authViewModel: AuthViewModel by viewModels()
    private val mapsViewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            DwelloTheme {
                MainScreen(
                    authViewModel = authViewModel,
                    mapsViewModel = mapsViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel, mapsViewModel: MapsViewModel) {
    val navController = rememberNavController()
//    val currentBackStack by navController.currentBackStackEntryAsState()
//    val currentDestination = currentBackStack?.destination
//    val currentScreen = bottomNavigationScreens.find { it.route == currentDestination?.route } ?: Screen.Home
    val isLoggedIn by authViewModel.user.collectAsState()
    var selectedTab by remember { mutableStateOf<Screen>(Screen.Home) }
    var redirectScreen by rememberSaveable { mutableStateOf<Screen?>(null)}

    Log.d("MainScreen", "MainScreen Composable rendered")

    LaunchedEffect(isLoggedIn) {
        redirectScreen?.let { screen ->
            if (isLoggedIn != null) {
                Log.d("MainScreen", "User logged in, redirecting to: ${screen.route}")

                // Attempt to pop the auth screen from the back stack
                val popped = navController.popBackStack(AuthScreen.Auth.route, inclusive = true)
                Log.d("MainScreen", "popBackStack result: $popped")

                // Navigate to the intended screen
                navController.navigateSingleTopTo(screen.route)
                redirectScreen = null

                // Log final back stack state
                navController.logBackStack()
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                allScreens = bottomNavigationScreens,
                onTabSelected = { newScreen ->
                    selectedTab = newScreen
                    if (isLoggedIn != null || newScreen == Screen.Home) {
                        navController.navigateSingleTopTo(newScreen.route)
                    } else {
                        redirectScreen = newScreen
                        Log.d("MainScreen", "User not logged in, redirecting to AuthScreen")
                        navController.navigateSingleTopTo(AuthScreen.Auth.route)
                    }
                },
                currentScreen = selectedTab,
            )
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            //isLoggedIn = isLoggedIn != null,
            authViewModel = authViewModel,
            //redirectScreen = redirectScreen,
            mapsViewModel = mapsViewModel
        )
    }
}