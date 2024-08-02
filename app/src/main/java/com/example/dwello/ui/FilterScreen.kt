package com.example.dwello.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dwello.ui.theme.DwelloTheme
import com.example.dwello.ui.theme.Red100

@Composable
fun FilterScreen(
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(onBackPressed, onResetFilters)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                FilterOptions()
            }
        }
        ApplyButton(onApplyFilters)
    }
}

@Composable
fun TopBar(
    onBackPressed: () -> Unit,
    onResetFilters: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Red100
            )
        }
        Text(
            text = "Filters",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            fontSize = 18.sp
        )
        TextButton(onClick = onResetFilters) {
            Text(
                text = "Reset",
                color = Red100,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
        }
    }
    // Top border line
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.Gray)
    )
}

@Composable
fun ApplyButton(onApplyFilters: () -> Unit) {
    // Top border line
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.Gray)
    )
    Button(
        onClick = onApplyFilters,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Red100),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = "Apply",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun FilterOptions() {
    Column {
        FilterSection(title = "Price") {
            PriceFilter()
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilterSection(title = "Bedrooms") {
            ExpandableMinMaxFilter(minOptions = bedroomOptions, maxOptions = bedroomOptions)
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilterSection(title = "Bathrooms") {
            BathroomsFilter()
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilterSection(title = "Home Type") {
            HomeTypeFilter()
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilterSection(title = "Square Feet") {
            MinMaxFilter(minOptions = squareFeetOptions, maxOptions = squareFeetOptions)
        }
        Spacer(modifier = Modifier.height(16.dp))
        FilterSection(title = "Year Built") {
            MinMaxFilter(minOptions = yearBuiltOptions, maxOptions = yearBuiltOptions)
        }
    }
}

@Composable
fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun PriceFilter() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = "",
            onValueChange = { /* Handle min price change */ },
            label = { Text("Enter Min") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = "-",
            style = MaterialTheme.typography.bodyMedium.copy(Color.Gray),
            fontSize = 30.sp,
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = "",
            onValueChange = { /* Handle max price change */ },
            label = { Text("Enter Max") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ExpandableMinMaxFilter(minOptions: List<String>, maxOptions: List<String>) {
    var expanded by remember { mutableStateOf(true) }

    Column {
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StandardDropdownMenu(minOptions, "No Min")
                Spacer(modifier = Modifier.width(8.dp))
                StandardDropdownMenu(maxOptions, "No Max")
            }
        }
    }
}

@Composable
fun MinMaxFilter(minOptions: List<String>, maxOptions: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StandardDropdownMenu(minOptions, "No Min")
        Spacer(modifier = Modifier.width(8.dp))
        StandardDropdownMenu(maxOptions, "No Max")
    }
}

@Composable
fun BathroomsFilter() {
    StandardDropdownMenu(listOf("Any", "1", "2", "3", "4", "5+"), "Any")
}

@Composable
fun HomeTypeFilter() {
    val homeTypes = listOf("Houses", "Apartments", "Townhouses", "Condos/Co-ops", "Lots/Land", "Multi-family", "Manufactured")
    val selectedTypes = remember { mutableStateListOf<String>() }

    Column {
        homeTypes.chunked(2).forEach { rowTypes ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowTypes.forEach { type ->
                    FilterChip(
                        label = type,
                        selected = selectedTypes.contains(type),
                        onSelectedChange = {
                            if (selectedTypes.contains(type)) {
                                selectedTypes.remove(type)
                            } else {
                                selectedTypes.add(type)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onSelectedChange: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onSelectedChange)
            .background(
                color = if (selected) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        if (selected) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (selected) Color.White else Color.Black
            )
        )
    }
}

@Composable
fun StandardDropdownMenu(options: List<String>, selectedOption: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption) }

    Box(
        modifier = Modifier
            .width(100.dp)
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            label = { Text("Select an option") },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = option
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}

val bedroomOptions = listOf("No Min", "1", "2", "3", "4", "5+")
val squareFeetOptions = listOf("No Min", "500", "1000", "1500", "2000", "2500+")
val yearBuiltOptions = listOf("No Min", "1980", "1990", "2000", "2010", "2020+")

@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    DwelloTheme {
        FilterScreen(
            onApplyFilters = { /* Handle Apply logic */ },
            onResetFilters = { /* Handle Reset logic */ },
            onBackPressed = { /* Handle back logic */ }
        )
    }
}