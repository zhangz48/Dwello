package com.example.dwello.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Property(
    @PropertyName("Document ID") val pid: String = "",
    @PropertyName("about_home") val about_home: String = "",
    @PropertyName("baths") val baths: Double = 0.0,
    @PropertyName("beds") val beds: Int = 0,
    @PropertyName("built_year") val built_year: Int = 0,
    @PropertyName("city") val city: String = "",
    @PropertyName("est_monthly") val est_monthly: Int = 0,
    @PropertyName("hoa") val hoa: Int = 0,
    @PropertyName("image_urls") val image_urls: List<String> = emptyList(),
    @PropertyName("list_date") val list_date: Timestamp = Timestamp.now(),
    @PropertyName("parking_space") val parking_space: Int = 0,
    @PropertyName("phone_number") val phone_number: String = "",
    @PropertyName("price") val price: Long = 0,
    @PropertyName("property_type") val property_type: String = "",
    @PropertyName("state") val state: String = "",
    @PropertyName("street") val street: String = "",
    @PropertyName("sqft") val sqft: Int = 0,
    @PropertyName("thumbnail_url") val thumbnail_url: String = "",
    @PropertyName("zipcode") val zipcode: String = "",
    @PropertyName("lat") val lat: Double = 0.0,
    @PropertyName("lng") val lng: Double = 0.0
)