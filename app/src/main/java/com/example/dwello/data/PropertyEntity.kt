package com.example.dwello.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.Timestamp
import java.time.LocalDateTime

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val pid: String,
    val about_home: String,
    val baths: Double,
    val beds: Int,
    val built_year: Int,
    val city: String,
    val est_monthly: Int,
    val hoa: Int,
    val image_urls: List<String>,
    val list_date: Long,
    val parking_space: Int,
    val phone_number: String,
    val price: Long,
    val property_type: String,
    val state: String,
    val street: String,
    val sqft: Int,
    val thumbnail_url: String,
    val zipcode: String,
    val lat: Double,
    val lng: Double
)
