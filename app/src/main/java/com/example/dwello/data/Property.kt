package com.example.dwello.data

import com.google.firebase.firestore.PropertyName
import java.time.LocalDateTime

data class Property(
    @PropertyName("Document ID") val pid: String = "",
    @PropertyName("about_home") val aboutHome: String = "",
    @PropertyName("baths") val baths: Double = 0.0,
    @PropertyName("beds") val beds: Int = 0,
    @PropertyName("built_year") val builtYear: Int = 0,
    @PropertyName("city") val city: String = "",
    @PropertyName("est_monthly") val estMonthly: Int = 0,
    @PropertyName("hoa") val hoa: Int = 0,
    @PropertyName("image_urls") val imageUrls: List<String> = emptyList(),
    @PropertyName("list_date") val listDate: LocalDateTime = LocalDateTime.now(),
    @PropertyName("parking_space") val parkingSpace: Int = 0,
    @PropertyName("price") val price: Long = 0,
    @PropertyName("property_type") val propertyType: String = "",
    @PropertyName("state") val state: String = "",
    @PropertyName("street") val street: String = "",
    @PropertyName("sqft") val sqft: Int = 0,
    @PropertyName("thumbnail_url") val thumbnailUrl: String = "",
    @PropertyName("zipcode") val zipcode: String = "",
    @PropertyName("lat") val lat: Double = 0.0,
    @PropertyName("lng") val lng: Double = 0.0
)