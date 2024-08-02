package com.example.dwello.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dwello.R
import com.example.dwello.data.Property
import com.example.dwello.ui.components.PropertyPreviewFav
import com.example.dwello.ui.components.mockPropertyViewModel
import com.example.dwello.ui.theme.DwelloTheme
import com.example.dwello.viewmodel.PropertyViewModel

@Composable
fun FavouritesScreen(
    propertyViewModel: PropertyViewModel,
    navController: NavController
) {
    Log.d("FavouritesScreen", "FavouritesScreen Composable rendered")

    val savedProperties by propertyViewModel.favouriteProperties.collectAsState()
    Log.d("FavouritesScreen", "Saved properties: ${savedProperties.size}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        TopBar(savedProperties.size)
        if (savedProperties.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You haven't saved any homes yet.\nStart exploring and add your favourites!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            SavedHomesList(
                properties = savedProperties,
                propertyViewModel = propertyViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun TopBar(homeCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My saved homes",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        IconButton(onClick = { /* Handle sort click */ }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort",
                tint = Color.Black
            )
        }
    }
    Text(
        text = "$homeCount homes",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
}

@Composable
fun SavedHomesList(
    properties: List<Property>,
    propertyViewModel: PropertyViewModel,
    navController: NavController) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(properties) { property ->
            PropertyPreviewFav(
                propertyViewModel = propertyViewModel,
                property = property,
                onClick = {
                    navController.navigate("property/${property.pid}")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    DwelloTheme {
        val mockProperties = listOf(
            Property(
                pid = "1",
                price = 1000000,
                beds = 4,
                baths = 6.5,
                sqft = 3710,
                street = "1111 NE 28th St",
                city = "Bellevue",
                state = "WA",
                zipcode = "98008",
                image_urls = listOf("https://via.placeholder.com/600x300")
            )
        )

        // Mocked or preview instances
        val mockNavController = NavController(LocalContext.current)
        val mockViewModel = mockPropertyViewModel() // Replace with a real or mock view model

        FavouritesScreen(
            propertyViewModel = mockViewModel,
            navController = mockNavController
        )
    }
}