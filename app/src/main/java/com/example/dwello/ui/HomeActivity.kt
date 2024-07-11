package com.example.dwello.ui

import android.util.Log
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.dwello.R
import com.example.dwello.fragments.MapsFragment
import com.example.dwello.ui.theme.DwelloTheme
import com.example.dwello.viewmodel.MapsViewModel
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.SupportMapFragment

@Composable
fun HomeScreen(fragmentManager: FragmentManager) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search and Filter section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp, 16.dp, 4.dp)
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
        // Map section using Fragment
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                FrameLayout(ctx).apply {
                    id = R.id.map_container
                    fragmentManager.beginTransaction()
                        .replace(this.id, MapsFragment())
                        .commit()
                }
            }
        )
        Log.d("HomeScreen", "Home screen called")
    }
}


//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    DwelloTheme {
//        HomeScreen()
//    }
//}