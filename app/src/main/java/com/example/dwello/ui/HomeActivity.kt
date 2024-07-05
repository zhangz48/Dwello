package com.example.dwello.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import com.example.dwello.ui.theme.DwelloTheme

@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search and Filter section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                singleLine = true,
                cursorBrush = SolidColor(Color.Black),
                decorationBox = { innerTextField ->
                    if (searchQuery.text.isEmpty()) {
                        Text(
                            text = "Search",
                            color = Color.Gray,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Filter logic */ }) {
                Text(text = "Filter")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* Save search logic */ }) {
                Text(text = "Save Search")
            }
        }
        // Map section (placeholder for actual map content)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFA1E6A1)) // Light green background to simulate map
        ) {
            // Placeholder for map markers

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DwelloTheme {
        HomeScreen()
    }
}