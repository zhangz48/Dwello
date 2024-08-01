package com.example.dwello.activities

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
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
import com.example.dwello.ui.PropertyListingScreen
import com.example.dwello.ui.SignUpScreen
import com.example.dwello.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.dwello.utils.logBackStack
import com.example.dwello.viewmodel.MapsViewModel
import com.example.dwello.viewmodel.PropertyViewModel

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Find Homes", Icons.Outlined.Home)
    object Favourites : Screen("favourites", "Favourites", Icons.Outlined.FavoriteBorder)
    object Account : Screen("account", "My Account", Icons.Outlined.Person)
    object PropertyListing : Screen("property/{propertyId}", "", Icons.Filled.Home) {
        fun createRoute(propertyId: String) = "property/$propertyId"
    }
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
    currentScreen: Screen,
    borderThickness: Dp = 0.5.dp
) {
    Box {
        // Top border line
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(borderThickness)
                .background(Color.Gray)
                .align(Alignment.TopCenter)
        )
        // NavigationBar content
        androidx.compose.material3.NavigationBar(
            modifier = Modifier
                .height(56.dp)
                .padding(top = borderThickness), // Adjust the height of the NavigationBar
            containerColor = Color.White,
            contentColor = Color.Gray
        ) {
            allScreens.forEach { screen ->
                val isSelected = currentScreen == screen
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(screen) },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(0.dp) // Adjust this value to control the spacing
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (isSelected) Color(0xFFC91818) else Color.Black,
                                modifier = Modifier.size(28.dp) // Adjust icon size
                            )
                            Text(
                                text = screen.title,
                                color = if (isSelected) Color(0xFFC91818) else Color.Black,
                                style = MaterialTheme.typography.labelSmall, // Customize text style
                                modifier = Modifier.padding(0.dp) // No padding around the label
                            )
                        }
                    },
                    modifier = Modifier.padding(6.dp), // No padding around the item
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFC91818),
                        selectedTextColor = Color(0xFFC91818),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent // Set to transparent to remove indicator
                    )
                )
            }
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    mapsViewModel: MapsViewModel,
    propertyViewModel: PropertyViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(mapsViewModel, propertyViewModel, navController)
        }
        composable(Screen.Favourites.route) {
            FavouritesScreen()
        }
        composable(Screen.Account.route) {
            AccountScreen(authViewModel, navController, propertyViewModel)
        }
        propertyNavGraph(navController, propertyViewModel)
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

fun NavGraphBuilder.propertyNavGraph(
    navController: NavHostController,
    propertyViewModel: PropertyViewModel
) {
    composable(Screen.PropertyListing.route) { backStackEntry ->
        val propertyId = backStackEntry.arguments?.getString("propertyId")
        val property = propertyViewModel.getPropertyById(propertyId)
        property?.let {
            PropertyListingScreen(navController, it)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = false
    }
    logBackStack() // Log the back stack after navigation
}