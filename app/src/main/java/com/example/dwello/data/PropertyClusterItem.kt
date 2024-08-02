package com.example.dwello.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class PropertyClusterItem(
    val property: Property
) : ClusterItem {
    override fun getPosition() = LatLng(property.lat, property.lng)
    override fun getTitle() = null // No title, so returning null
    override fun getSnippet() = null // No snippet, so returning null
    override fun getZIndex() = 0f
}