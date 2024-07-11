package com.example.dwello.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dwello.R
import com.example.dwello.ui.theme.DwelloTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 101

    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap

        // Add a marker in New York and move the camera
        val defaultPos = LatLng(40.7484, -73.9857)
        googleMap.addMarker(MarkerOptions().position(defaultPos).title("Marker in New York"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPos, 16f))
        Log.d("MapsFragment", "Map is ready and camera is moved to default location")
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MapsFragment", "Permission granted")
            getCurrentLocation()
        } else {
            Log.e("MapsFragment", "Permission denied")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MapsFragment", "onCreate called")

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)

        // Set up Compose
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            DwelloTheme {
                MapScreen()
            }
        }

        return view
    }

    @Composable
    fun MapScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NearbyButton()
                LayersButton()
            }
        }
    }

    @Composable
    fun NearbyButton() {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color.White,
            contentColor = Color.Black
        ) {
            IconButton(onClick = {
                Log.d("MapsFragment", "Nearby button clicked")
                when {
                    ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        Log.d("MapsFragment", "Permission already granted")
                        getCurrentLocation()
                    }

                    else -> {
                        Log.d("MapsFragment", "Requesting permission")
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Nearby",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    @Composable
    fun LayersButton() {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Color.White,
            contentColor = Color.Black
        ) {
            IconButton(onClick = { onLayersButtonClick() }) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Layers",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }

    private fun getCurrentLocation() {
        Log.d("MapsFragment", "Getting current location")
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("MapsFragment", "Permission not granted")
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                Log.d("MapsFragment", "Moved camera to current location: $currentLatLng")
            } else {
                Log.e("MapsFragment", "Location is null")
            }
        }.addOnFailureListener { e ->
            Log.e("MapsFragment", "Failed to get location: ${e.message}")
        }
    }

    private fun onLayersButtonClick() {
        // Toggle between different map types
        googleMap.mapType = when (googleMap.mapType) {
            GoogleMap.MAP_TYPE_NORMAL -> GoogleMap.MAP_TYPE_HYBRID
            GoogleMap.MAP_TYPE_HYBRID -> GoogleMap.MAP_TYPE_NORMAL
            else -> GoogleMap.MAP_TYPE_NORMAL
        }
    }

}