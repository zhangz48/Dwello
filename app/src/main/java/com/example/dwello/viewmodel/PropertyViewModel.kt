package com.example.dwello.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dwello.BuildConfig
import com.example.dwello.data.Property
import com.example.dwello.data.PropertyEntity
import com.example.dwello.data.toDomain
import com.example.dwello.data.toEntity
import com.example.dwello.utils.DatabaseProvider
import com.example.dwello.utils.GeocodingApiService
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PropertyViewModel(context: Context) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val database = DatabaseProvider.getDatabase(context)

    // Expose properties as StateFlow
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> get() = _properties

    private val apiKey = BuildConfig.MAPS_API_KEY

    private val retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val geocodingApiService = retrofit.create(GeocodingApiService::class.java)

    private var propertyListener: ListenerRegistration? = null

    init {
        fetchProperties()
        listenForPropertyUpdates()
    }

    fun getPropertyById(propertyId: String?): Property? {
        return properties.value.find { it.pid == propertyId }
    }

    fun fetchProperties() {
        viewModelScope.launch {
            // Load cached properties first
            val cachedProperties = database.propertyDao().getAllProperties()
            Log.d("PropertyViewModel", "Cached properties: $cachedProperties")
            _properties.value = cachedProperties.map { it.toDomain() }

            // Fetch properties from Firestore
            firestore.collection("properties").get().addOnSuccessListener { documents ->
                val fetchedProperties = mutableListOf<PropertyEntity>()
                for (document in documents) {
                    var property = document.toObject(Property::class.java).copy(pid = document.id)

                    if (property.lat == 0.0 && property.lng == 0.0) {
                        val address =
                            "${property.street}, ${property.city}, ${property.state}, ${property.zipcode}"
                        viewModelScope.launch {
                            val latLng = getLatLonForAddress(address)
                            latLng?.let { latLngValue ->
                                property = property.copy(
                                    lat = latLngValue.latitude, lng = latLngValue.longitude
                                )
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
                }
            }.addOnFailureListener { exception ->
                Log.w("PropertyViewModel", "Error getting documents: ", exception)
            }
        }
    }

    private fun listenForPropertyUpdates() {
        propertyListener = firestore.collection("properties")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("PropertyViewModel", "listen:error", e)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val updatedProperties = mutableListOf<PropertyEntity>()
                    for (document in snapshots.documents) {
                        var property = document.toObject(Property::class.java)?.copy(pid = document.id)

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
                    }
                }
            }
    }

    fun clearLocalDatabase() {
        viewModelScope.launch {
            database.propertyDao().deleteAll()
            _properties.value = emptyList()
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
            Log.e("PropertyViewModel", "Geocoding API error: ${e.message}")
            null
        }
    }
}