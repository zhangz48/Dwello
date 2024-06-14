package com.example.dwello

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen("home", "Find Homes", Icons.Default.Home)
    object Favourites : Screen("favourites", "Favourites", Icons.Default.Favorite)
    object Account : Screen("account", "My Account", Icons.Default.Person)
}

val bottomNavigationScreens = listOf(Screen.Home, Screen.Favourites, Screen.Account)

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
                    indicatorColor = Color.White,
                )
            )
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = Screen.Home.route, modifier = modifier) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Favourites.route) { FavouritesScreen() }
        composable(Screen.Account.route) { AccountScreen() }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }