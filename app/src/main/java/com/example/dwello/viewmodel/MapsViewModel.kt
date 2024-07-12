package com.example.dwello.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class MapsViewModel(context: Context) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val currentLocation = mutableStateOf<Location?>(null)
    val permissionDenied = mutableStateOf(false)
    val isMyLocationEnabled = mutableStateOf(false)

    fun getCurrentLocation(context: Context) {
        viewModelScope.launch {
            try {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnSuccessListener { location ->
                    currentLocation.value = location
                }
            } catch (e: SecurityException) {
                // Handle exception if location permission is not granted
            }
        }
    }

    fun enableMyLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isMyLocationEnabled.value = true
        } else {
            isMyLocationEnabled.value = false
            permissionDenied.value = true
        }
    }

    fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        context: Context
    ) {
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            isMyLocationEnabled.value = true
            getCurrentLocation(context)
        } else {
            // Permission was denied.
            permissionDenied.value = true
        }
    }

    private fun isPermissionGranted(
        permissions: Array<String>,
        grantResults: IntArray,
        permission: String
    ): Boolean {
        for (i in permissions.indices) {
            if (permissions[i] == permission) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }
}