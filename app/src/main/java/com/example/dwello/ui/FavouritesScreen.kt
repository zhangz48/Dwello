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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var sortOption by remember { mutableStateOf("Saved Date (recent to old)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        TopBar(savedProperties.size,
            sortOption,
            onSortOptionSelected = { option ->
            sortOption = option
            propertyViewModel.sortProperties(option) }
        )
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
fun TopBar(
    homeCount: Int,
    sortOption: String,
    onSortOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
        Box {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Sort",
                    tint = Color.Black
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Saved Date (recent to old)") },
                    onClick = {
                        onSortOptionSelected("Saved Date (recent to old)")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Saved Date (recent to old)") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Saved Date (old to recent)") },
                    onClick = {
                        onSortOptionSelected("Saved Date (old to recent)")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Saved Date (old to recent)") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Price (high to low)") },
                    onClick = {
                        onSortOptionSelected("Price (high to low)")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Price (high to low)") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Price (low to high)") },
                    onClick = {
                        onSortOptionSelected("Price (low to high)")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Price (low to high)") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Square Feet") },
                    onClick = {
                        onSortOptionSelected("Square Feet")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Square Feet") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text("Newest Built") },
                    onClick = {
                        onSortOptionSelected("Newest Built")
                        expanded = false
                    },
                    trailingIcon = {
                        if (sortOption == "Newest Built") {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }
        }
    }
    Text(
        text = "$homeCount homes",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Black
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