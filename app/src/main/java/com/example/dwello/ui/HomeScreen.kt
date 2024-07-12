package com.example.dwello.ui

import android.util.Log
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currentplacedetailsonmap.MapScreen
import com.example.dwello.R
import com.example.dwello.viewmodel.MapsViewModel

@Composable
fun HomeScreen(mapsViewModel: MapsViewModel) {
    Log.d("HomeScreen", "HomeScreen Composable rendered")
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Search and Filter section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0x1F767680), shape = RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchQuery.text.isEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Search",
                                    color = Color.Gray,
                                )
                            }
                        }
                        innerTextField()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                        .padding(end = 4.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    Button(
                        onClick = { /* Filter logic */ },
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Filter",
                            style = LocalTextStyle.current.copy(
                                fontSize = 14.sp
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Button(
                        onClick = { /* Save search logic */ },
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Save Search",
                            style = LocalTextStyle.current.copy(
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Border line before the MapScreen
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(Color.Gray)
        )

        // Map section using Compose
        MapScreen(viewModel = mapsViewModel)
    }
}