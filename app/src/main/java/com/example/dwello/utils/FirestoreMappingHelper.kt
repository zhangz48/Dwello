package com.example.dwello.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.Timestamp
import com.example.dwello.data.Property
import java.time.LocalDateTime
import java.time.ZoneId

fun DocumentSnapshot.toProperty(): Property {
    return Property(
        pid = this.id,
        about_home = this.getString("about_home") ?: "",
        baths = this.getDouble("baths") ?: 0.0,
        beds = (this.getLong("beds") ?: 0).toInt(),
        built_year = (this.getLong("built_year") ?: 0).toInt(),
        city = this.getString("city") ?: "",
        est_monthly = (this.getLong("est_monthly") ?: 0).toInt(),
        hoa = (this.getLong("hoa") ?: 0).toInt(),
        image_urls = this.get("image_urls") as? List<String> ?: emptyList(),
        list_date = this.getTimestamp("list_date") ?: Timestamp.now(),
        parking_space = (this.getLong("parking_space") ?: 0).toInt(),
        price = this.getLong("price") ?: 0L,
        property_type = this.getString("property_type") ?: "",
        state = this.getString("state") ?: "",
        street = this.getString("street") ?: "",
        sqft = (this.getLong("sqft") ?: 0).toInt(),
        thumbnail_url = this.getString("thumbnail_url") ?: "",
        zipcode = this.getString("zipcode") ?: "",
        lat = this.getDouble("lat") ?: 0.0,
        lng = this.getDouble("lng") ?: 0.0
    )
}

private fun Timestamp.toLocalDateTime(): LocalDateTime {
    return this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}