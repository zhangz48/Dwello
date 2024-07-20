package com.example.dwello.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dwello.BuildConfig
import com.example.dwello.data.Property
import com.example.dwello.data.PropertyEntity
import com.example.dwello.data.toDomain
import com.example.dwello.data.toEntity
import com.example.dwello.utils.DatabaseProvider
import com.example.dwello.utils.GeocodingApiService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsViewModel(context: Context) : ViewModel() {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val currentLocation = mutableStateOf<Location?>(null)
    val permissionDenied = mutableStateOf(false)
    val isMyLocationEnabled = mutableStateOf(false)
    val selectedPlace = mutableStateOf<Place?>(null)

    private val firestore = FirebaseFirestore.getInstance()
    private val database = DatabaseProvider.getDatabase(context)

    // Expose properties as StateFlow
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> get() = _properties

//    val properties = mutableStateListOf<Property>()

    private val apiKey = BuildConfig.MAPS_API_KEY

    private val retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val geocodingApiService = retrofit.create(GeocodingApiService::class.java)

    private var propertyListener: ListenerRegistration? = null

    init {
        fetchProperties()
        listenForPropertyUpdates()
    }

    fun updateSelectedPlace(place: Place) {
        selectedPlace.value = place
    }

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
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isMyLocationEnabled.value = true
        } else {
            isMyLocationEnabled.value = false
            permissionDenied.value = true
        }
    }

    fun onRequestPermissionsResult(
        permissions: Array<String>, grantResults: IntArray, context: Context
    ) {
        if (isPermissionGranted(
                permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION
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
        permissions: Array<String>, grantResults: IntArray, permission: String
    ): Boolean {
        for (i in permissions.indices) {
            if (permissions[i] == permission) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }

    fun fetchProperties() {
        viewModelScope.launch {
            // Load cached properties first
            val cachedProperties = database.propertyDao().getAllProperties()
            _properties.value = cachedProperties.map { it.toDomain() }
//            properties.clear()
//            properties.addAll(cachedProperties.map { it.toDomain() })

            // Fetch properties from Firestore
            firestore.collection("properties").get().addOnSuccessListener { documents ->
                val fetchedProperties = mutableListOf<PropertyEntity>()
                for (document in documents) {
                    var property = document.toObject(Property::class.java).copy(pid = document.id)

                    // Log each property to verify correct fetching
                    Log.d("MapsViewModel", "Fetched property: $property")

                    if (property.lat == 0.0 && property.lng == 0.0) {
                        val address =
                            "${property.street}, ${property.city}, ${property.state}, ${property.zipcode}"
                        viewModelScope.launch {
                            val latLng = getLatLonForAddress(address)
                            latLng?.let { latLngValue ->
                                property = property.copy(
                                    lat = latLngValue.latitude, lng = latLngValue.longitude
                                )
                                // Update property in Firestore with the new lat/lng
                                firestore.collection("properties").document(property.pid).set(property)
                            }
                        }
                    }
                    fetchedProperties.add(property.toEntity())
                }
                viewModelScope.launch {
                    database.propertyDao().deleteAll()
                    database.propertyDao().insertAll(fetchedProperties)
                    _properties.value = fetchedProperties.map { it.toDomain() }
//                    properties.clear()
//                    properties.addAll(fetchedProperties.map { it.toDomain() })
                }
            }.addOnFailureListener { exception ->
                Log.w("MapsViewModel", "Error getting documents: ", exception)
            }
        }
    }

    private fun listenForPropertyUpdates() {
        propertyListener = firestore.collection("properties")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("MapsViewModel", "listen:error", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val updatedProperties = mutableListOf<PropertyEntity>()
                    for (document in snapshots.documents) {
                        var property = document.toObject(Property::class.java)?.copy(pid = document.id)

                        // Log each property to verify correct fetching
                        Log.d("MapsViewModel", "Real-time update property: $property")

                        if (property != null) {
                            if (property.lat == 0.0 && property.lng == 0.0) {
                                val address =
                                    "${property.street}, ${property.city}, ${property.state}, ${property.zipcode}"
                                viewModelScope.launch {
                                    val latLng = getLatLonForAddress(address)
                                    latLng?.let { latLngValue ->
                                        property = property!!.copy(
                                            lat = latLngValue.latitude, lng = latLngValue.longitude
                                        )
                                        // Update property in Firestore with the new lat/lng
                                        firestore.collection("properties").document(property!!.pid).set(
                                            property!!
                                        )
                                    }
                                }
                            }
                            updatedProperties.add(property!!.toEntity())
                        }
                    }
                    viewModelScope.launch {
                        database.propertyDao().deleteAll()
                        database.propertyDao().insertAll(updatedProperties)
                        _properties.value = updatedProperties.map { it.toDomain() }
//                        properties.clear()
//                        properties.addAll(updatedProperties.map { it.toDomain() })
                    }
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        propertyListener?.remove()
    }

    suspend fun getLatLonForAddress(address: String): LatLng? {
        return try {
            val response = geocodingApiService.getGeocoding(address, apiKey)
            if (response.results.isNotEmpty()) {
                val location = response.results[0].geometry.location
                LatLng(location.lat, location.lng)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MapsViewModel", "Geocoding API error: ${e.message}")
            null
        }
    }
}