package com.example.currentplacedetailsonmap

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dwello.activities.Screen
import com.example.dwello.data.Property
import com.example.dwello.ui.components.PropertyClusteringMapContent
import com.example.dwello.ui.components.PropertyPreview
import com.example.dwello.ui.theme.*
import com.example.dwello.ui.components.formatPrice
import com.example.dwello.ui.components.setCustomMapIcon
import com.example.dwello.viewmodel.MapsViewModel
import com.example.dwello.viewmodel.PropertyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(mapsViewModel: MapsViewModel,
              propertyViewModel: PropertyViewModel,
              navController: NavController) {
    Log.d("MapScreen", "MapScreen Composable rendered")

    val context = LocalContext.current

    // Fetch properties from Firebase and update cache when the composable is first launched
    LaunchedEffect(Unit) {
        propertyViewModel.fetchProperties()
    }

    // Remember the properties as state
    val properties by propertyViewModel.properties.collectAsState()

    // Track selected marker
    var selectedProperty by remember { mutableStateOf<Property?>(null) }

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
        position = CameraPosition.fromLatLngZoom(LatLng(47.6101, -122.2015), defaultZoomLevel)
    }

    // Function to center the map on the user's location
    val centerMapOnUserLocation: () -> Unit = {
        if (mapsViewModel.currentLocation.value != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(
                    mapsViewModel.currentLocation.value!!.latitude,
                    mapsViewModel.currentLocation.value!!.longitude
                ), defaultZoomLevel
            )
        }
    }

    // Ensure permissions are granted and location is fetched
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            mapsViewModel.getCurrentLocation(context)
            mapsViewModel.enableMyLocation(context)
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
        GoogleMap(modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = mapsViewModel.isMyLocationEnabled.value, mapType = mapType
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                rotationGesturesEnabled = true
            ),
            onMapLoaded = {

            },
            onMapClick = {
                selectedProperty = null
            }) {
            // PropertyClusteringMapContent(properties = properties)
            // Display markers for each property
            properties.forEach { property ->
                val isSelected = property == selectedProperty
                val icon = setCustomMapIcon(formatPrice(property.price), isSelected)

                Marker(state = MarkerState(position = LatLng(property.lat, property.lng)),
                    title = null,
                    snippet = null,
                    icon = icon,
                    onClick = {
                        selectedProperty = property
                        true
                    })
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 36.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
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
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Layers,
                        contentDescription = "Layers",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = LocationButtonColor,
                contentColor = LocationIconColor
            ) {
                IconButton(onClick = {
                    isLocationButtonBlinking = true
                    if (locationPermissionsState.allPermissionsGranted) {
                        mapsViewModel.enableMyLocation(context)
                        mapsViewModel.getCurrentLocation(context)
                        centerMapOnUserLocation()
                    } else {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        if (!locationPermissionsState.allPermissionsGranted) {
                            Toast.makeText(
                                context,
                                "Enable location for the Dwello app in Settings.",
                                Toast.LENGTH_LONG
                            ).show()
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

        // Display the selected property preview
        if (selectedProperty != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp), // Adjust the padding to move it a bit up from the bottom
                contentAlignment = Alignment.BottomCenter // Center the PropertyPreview at the bottom
            ) {
                PropertyPreview(propertyViewModel = propertyViewModel, property = selectedProperty!!) {
                    navController.navigate(Screen.PropertyListing.createRoute(selectedProperty!!.pid))
                }
            }
        }
    }

    // Observe permission denial state and show a toast if permissions are denied
    if (mapsViewModel.permissionDenied.value) {
        Toast.makeText(
            context, "Enable location for the Dwello app in Settings.", Toast.LENGTH_LONG
        ).show()
        mapsViewModel.permissionDenied.value = false
    }
}