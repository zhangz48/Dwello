package com.example.dwello.data

import com.example.dwello.data.Property
import com.example.dwello.data.PropertyEntity
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

fun PropertyEntity.toDomain(): Property {
    return Property(
        pid = this.pid,
        about_home = this.about_home,
        baths = this.baths,
        beds = this.beds,
        built_year = this.built_year,
        city = this.city,
        est_monthly = this.est_monthly,
        hoa = this.hoa,
        image_urls = this.image_urls,
        list_date = Timestamp(this.list_date, 0),
        parking_space = this.parking_space,
        phone_number = this.phone_number,
        price = this.price,
        property_type = this.property_type,
        state = this.state,
        street = this.street,
        sqft = this.sqft,
        thumbnail_url = this.thumbnail_url,
        zipcode = this.zipcode,
        lat = this.lat,
        lng = this.lng
    )
}

fun Property.toEntity(): PropertyEntity {
    return PropertyEntity(
        pid = this.pid,
        about_home = this.about_home,
        baths = this.baths,
        beds = this.beds,
        built_year = this.built_year,
        city = this.city,
        est_monthly = this.est_monthly,
        hoa = this.hoa,
        image_urls = this.image_urls,
        list_date = this.list_date.seconds,
        parking_space = this.parking_space,
        phone_number = this.phone_number,
        price = this.price,
        property_type = this.property_type,
        state = this.state,
        street = this.street,
        sqft = this.sqft,
        thumbnail_url = this.thumbnail_url,
        zipcode = this.zipcode,
        lat = this.lat,
        lng = this.lng
    )
}