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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PropertyViewModel(context: Context) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val database = DatabaseProvider.getDatabase(context)
    private val auth = FirebaseAuth.getInstance()

    // Expose properties as StateFlow
    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> get() = _properties

    // Expose favorite properties as StateFlow
    private val _favouriteProperties = MutableStateFlow<List<Property>>(emptyList())
    val favouriteProperties: StateFlow<List<Property>> get() = _favouriteProperties

    // Cache favourite status in a Map
    private val _favouriteStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val favouriteStatus: StateFlow<Map<String, Boolean>> get() = _favouriteStatus

    private val apiKey = BuildConfig.MAPS_API_KEY

    private val retrofit = Retrofit.Builder().baseUrl("https://maps.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val geocodingApiService = retrofit.create(GeocodingApiService::class.java)

    private var propertyListener: ListenerRegistration? = null

    init {
        fetchProperties()
        listenForPropertyUpdates()
        fetchFavouriteProperties()
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

    // Add a property to user's favourites in the saved_listings field
    fun addPropertyToFavourites(propertyId: String) {
        val currentUser = auth.currentUser ?: return
        val userRef = firestore.collection("users").document(currentUser.uid)

        viewModelScope.launch {
            try {
                userRef.update("saved_listings", FieldValue.arrayUnion(propertyId)).await()
                _favouriteStatus.value = _favouriteStatus.value.toMutableMap().apply {
                    this[propertyId] = true
                }
                fetchFavouriteProperties()
                Log.d("PropertyViewModel", "Property added to favourites")
            } catch (e: Exception) {
                Log.e("PropertyViewModel", "Failed to add property to favourites", e)
            }
        }
    }

    // Remove a property from user's favourites in the saved_listings field
    fun removePropertyFromFavourites(propertyId: String) {
        val currentUser = auth.currentUser ?: return
        val userRef = firestore.collection("users").document(currentUser.uid)

        viewModelScope.launch {
            try {
                userRef.update("saved_listings", FieldValue.arrayRemove(propertyId)).await()
                _favouriteStatus.value = _favouriteStatus.value.toMutableMap().apply {
                    this[propertyId] = false
                }
                fetchFavouriteProperties()
                Log.d("PropertyViewModel", "Property removed from favourites")
            } catch (e: Exception) {
                Log.e("PropertyViewModel", "Failed to remove property from favourites", e)
            }
        }
    }

    fun isPropertyFavourited(propertyId: String): StateFlow<Boolean> {
        return _favouriteStatus.map { it[propertyId] ?: false }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            false
        )
    }

    // Update favourite properties and cache the status
    fun fetchFavouriteProperties() {
        val currentUser = auth.currentUser ?: return
        val userRef = firestore.collection("users").document(currentUser.uid)

        viewModelScope.launch {
            try {
                // Fetch the saved_listings array from the user's document
                val userDoc = userRef.get().await()
                val favouriteIds = userDoc["saved_listings"] as? List<String> ?: emptyList()

                if (favouriteIds.isNotEmpty()) {
                    val propertiesRef = firestore.collection("properties")
                    val propertiesSnapshot = propertiesRef.whereIn(FieldPath.documentId(), favouriteIds).get().await()
                    val properties = propertiesSnapshot.documents.map { doc ->
                        doc.toObject(Property::class.java)?.copy(pid = doc.id)
                    }.filterNotNull()

                    _favouriteStatus.value = favouriteIds.associateWith { true }
                    _favouriteProperties.value = properties
                } else {
                    _favouriteStatus.value = emptyMap()
                    _favouriteProperties.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("PropertyViewModel", "Failed to load favourite properties", e)
            }
        }
    }
}