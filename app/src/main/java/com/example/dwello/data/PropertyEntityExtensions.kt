package com.example.dwello.data

fun PropertyEntity.toDomain(): Property {
    return Property(
        pid = this.pid,
        aboutHome = this.aboutHome,
        baths = this.baths,
        beds = this.beds,
        builtYear = this.builtYear,
        city = this.city,
        estMonthly = this.estMonthly,
        hoa = this.hoa,
        imageUrls = this.imageUrls,
        listDate = this.listDate,
        parkingSpace = this.parkingSpace,
        price = this.price,
        propertyType = this.propertyType,
        state = this.state,
        street = this.street,
        sqft = this.sqft,
        thumbnailUrl = this.thumbnailUrl,
        zipcode = this.zipcode,
        lat = this.lat,
        lng = this.lng
    )
}

fun Property.toEntity(): PropertyEntity {
    return PropertyEntity(
        pid = this.pid,
        aboutHome = this.aboutHome,
        baths = this.baths,
        beds = this.beds,
        builtYear = this.builtYear,
        city = this.city,
        estMonthly = this.estMonthly,
        hoa = this.hoa,
        imageUrls = this.imageUrls,
        listDate = this.listDate,
        parkingSpace = this.parkingSpace,
        price = this.price,
        propertyType = this.propertyType,
        state = this.state,
        street = this.street,
        sqft = this.sqft,
        thumbnailUrl = this.thumbnailUrl,
        zipcode = this.zipcode,
        lat = this.lat,
        lng = this.lng
    )
}