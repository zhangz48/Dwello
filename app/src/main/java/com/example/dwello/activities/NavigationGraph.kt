package com.example.dwello.activities

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.dwello.ui.AccountScreen
import com.example.dwello.ui.AuthScreen
import com.example.dwello.ui.FavouritesScreen
import com.example.dwello.ui.HomeScreen
import com.example.dwello.ui.SignUpScreen
import com.example.dwello.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.dwello.utils.logBackStack
import com.example.dwello.viewmodel.MapsViewModel

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Find Homes", Icons.Default.Home)
    object Favourites : Screen("favourites", "Favourites", Icons.Default.Favorite)
    object Account : Screen("account", "My Account", Icons.Default.Person)
}

sealed class AuthScreen(val route: String) {
    object Auth : AuthScreen("auth")
    object SignUp : AuthScreen("signup")
}

val bottomNavigationScreens = listOf(
    Screen.Home,
    Screen.Favourites,
    Screen.Account
)

@Composable
fun NavigationBar(
    allScreens: List<Screen>,
    onTabSelected: (Screen) -> Unit,
    currentScreen: Screen
) {
    androidx.compose.material3.NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        allScreens.forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.title,
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                    )
                },
                selected = isSelected,
                onClick = { onTabSelected(screen) },
                modifier = Modifier.padding(3.dp),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFC91818),
                    selectedTextColor = Color(0xFFC91818),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    authViewModel: AuthViewModel,
    redirectScreen: Screen?,
    fragmentManager: FragmentManager
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(fragmentManager)
        }
        composable(Screen.Favourites.route) {
            FavouritesScreen()
        }
        composable(Screen.Account.route) {
            AccountScreen(authViewModel, navController)
        }
        authNavGraph(navController, authViewModel)
    }
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
) {
    navigation(
        startDestination = AuthScreen.Auth.route,
        route = "auth_graph"
    ) {
        composable(AuthScreen.Auth.route) {
            AuthScreen(
                onSignUpClick = { navController.navigate(AuthScreen.SignUp.route) },
                onSignInClick = { email, password ->
                    authViewModel.signIn(email, password)
                },
                authViewModel = authViewModel
            )
        }
        composable(AuthScreen.SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onSignUpSuccess = { navController.navigate(AuthScreen.Auth.route) },
                authViewModel = authViewModel
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    logBackStack() // Log the back stack after navigation
}