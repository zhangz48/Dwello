package com.example.dwello.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.dwello.ui.theme.DwelloTheme

//class FavouritesActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            DwelloTheme {
//                FavouritesScreen()
//            }
//        }
//    }
//}

@Composable
fun FavouritesScreen() {
        // Show the Favourites Screen content
        Text("Favourites Screen")
}