package com.example.dwello.utils

import retrofit2.http.GET
import retrofit2.http.Query

import com.google.gson.annotations.SerializedName

interface GeocodingApiService {
    @GET("maps/api/geocode/json")
    suspend fun getGeocoding(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}

data class GeocodingResponse(
    @SerializedName("results") val results: List<GeocodingResult>
)

data class GeocodingResult(
    @SerializedName("geometry") val geometry: Geometry
)

data class Geometry(
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)