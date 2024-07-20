package com.example.dwello.data

import com.google.maps.android.ResponseStreetView
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

data class Property(
    val pid: String = "",
    val aboutHome: String = "",
    val baths: Double = 0.0,
    val beds: Int = 0,
    val builtYear: Int = 0,
    val city: String = "",
    val estMonthly: Int = 0,
    val hoa: Int = 0,
    val imageUrls: List<String> = emptyList(),
    val listDate: LocalDateTime = LocalDateTime.now(),
    val parkingSpace: Int = 0,
    val price: Long = 0,
    val propertyType: String = "",
    val state: String = "",
    val street: String = "",
    val sqft: Int = 0,
    val thumbnailUrl: String = "",
    val zipcode: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)
