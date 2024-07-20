package com.example.dwello.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.time.LocalDateTime

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey val pid: String,
    val aboutHome: String,
    val baths: Double,
    val beds: Int,
    val builtYear: Int,
    val city: String,
    val estMonthly: Int,
    val hoa: Int,
    val imageUrls: List<String>,
    val listDate: LocalDateTime,
    val parkingSpace: Int,
    val price: Long,
    val propertyType: String,
    val state: String,
    val street: String,
    val sqft: Int,
    val thumbnailUrl: String,
    val zipcode: String,
    val lat: Double,
    val lng: Double
)
