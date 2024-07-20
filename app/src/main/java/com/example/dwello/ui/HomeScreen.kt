package com.example.dwello.ui

import android.util.Log
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currentplacedetailsonmap.MapScreen
import com.example.dwello.BuildConfig
import com.example.dwello.R
import com.example.dwello.ui.theme.*
import com.example.dwello.viewmodel.MapsViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

@Composable
fun HomeScreen(mapsViewModel: MapsViewModel) {
    Log.d("HomeScreen", "HomeScreen Composable rendered")

    var isMapMode by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val fragmentManager = (context as FragmentActivity).supportFragmentManager

    // Add AutocompleteSupportFragment
    val autocompleteFragment by remember {
        mutableStateOf(
            fragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
                ?: AutocompleteSupportFragment.newInstance().also { fragment ->
                    fragmentManager.commit {
                        replace(R.id.autocomplete_fragment, fragment)
                    }
                }
        )
    }

    LaunchedEffect(autocompleteFragment) {
        autocompleteFragment.apply {
            setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
            setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    Log.i("HomeScreen", "Place: ${place.name}, ${place.id}")
                    // Update the map with the selected place
                    mapsViewModel.updateSelectedPlace(place)
                }

                override fun onError(status: com.google.android.gms.common.api.Status) {
                    Log.i("HomeScreen", "An error occurred: $status")
                }
            })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Box with border for the AutocompleteSupportFragment
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .background(GreyText)
                .border(1.dp, GreyText, RoundedCornerShape(4.dp))
        ) {
            AndroidView(
                factory = { context ->
                    FrameLayout(context).apply {
                        id = R.id.autocomplete_fragment
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 4.dp, 16.dp, 4.dp)
        ) {
            // Filter and Swipe/Map buttons
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
                        onClick = {
                        /* Swipe/Map logic */
                            isMapMode = !isMapMode
                        },
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (isMapMode) "Swipe" else "Map",
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

        // Screen section based on mode
        if (isMapMode) {
            MapScreen(viewModel = mapsViewModel)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Swipe Screen Placeholder",
                    style = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}