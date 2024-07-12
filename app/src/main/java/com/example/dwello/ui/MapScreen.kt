package com.example.currentplacedetailsonmap

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.dwello.ui.theme.*
import com.example.dwello.ui.theme.DaySkyBlue
import com.example.dwello.viewmodel.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapsViewModel) {
    Log.d("MapScreen", "MapScreen Composable rendered")

    val context = LocalContext.current

    // Define the map type state
    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    val defaultZoomLevel = 15f

    // Define the permission state for location
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Define the camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.7484, -73.9857), defaultZoomLevel)
    }

    // Function to center the map on the user's location
    val centerMapOnUserLocation: () -> Unit = {
        if (viewModel.currentLocation.value != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(viewModel.currentLocation.value!!.latitude, viewModel.currentLocation.value!!.longitude),
                defaultZoomLevel
            )
        }
    }

    // Ensure permissions are granted and location is fetched
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            viewModel.getCurrentLocation(context)
            viewModel.enableMyLocation(context)
        }
    }

    var isLayersButtonBlinking by remember { mutableStateOf(false) }
    var isLocationButtonBlinking by remember { mutableStateOf(false) }

    val LayersButtonColor by animateColorAsState(
        targetValue = if (isLayersButtonBlinking) LightBlue else Color.White,
//        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )
    val LayersIconColor by animateColorAsState(
        targetValue = if (isLayersButtonBlinking) BrightBlue else Color.Black,
//        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )

    val LocationButtonColor by animateColorAsState(
        targetValue = if (isLocationButtonBlinking) LightBlue else Color.White,
//        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )
    val LocationIconColor by animateColorAsState(
        targetValue = if (isLocationButtonBlinking) BrightBlue else Color.Black,
//        animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )

    LaunchedEffect(isLayersButtonBlinking) {
        if (isLayersButtonBlinking) {
            delay(100)
            isLayersButtonBlinking = false
        }
    }

    LaunchedEffect(isLocationButtonBlinking) {
        if (isLocationButtonBlinking) {
            delay(100)
            isLocationButtonBlinking = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = viewModel.isMyLocationEnabled.value,
                mapType = mapType
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false
            ),
            onMapLoaded = {
                // Optionally, do something when the map is loaded
            }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 36.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = LayersButtonColor,
                contentColor = LayersIconColor
            ) {
                IconButton(onClick = {
                    isLayersButtonBlinking = true
                    // Toggle between different map types
                    mapType = if (mapType == MapType.NORMAL) {
                        MapType.HYBRID
                    } else {
                        MapType.NORMAL
                    }
                }
                )
                {
                    Icon(
                        imageVector = Icons.Outlined.Layers,
                        contentDescription = "Layers",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                color = LocationButtonColor,
                contentColor = LocationIconColor
            ) {
                IconButton(onClick = {
                    isLocationButtonBlinking = true
                    if (locationPermissionsState.allPermissionsGranted) {
                        viewModel.enableMyLocation(context)
                        viewModel.getCurrentLocation(context)
                        centerMapOnUserLocation()
                    } else {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        if (!locationPermissionsState.allPermissionsGranted) {
                            Toast.makeText(context, "Enable location for the Dwello app in Settings.", Toast.LENGTH_LONG).show()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.NearMe,
                        contentDescription = "My Location",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }

    // Observe permission denial state and show a toast if permissions are denied
    if (viewModel.permissionDenied.value) {
        Toast.makeText(context, "Enable location for the Dwello app in Settings.", Toast.LENGTH_LONG).show()
        viewModel.permissionDenied.value = false
    }
}