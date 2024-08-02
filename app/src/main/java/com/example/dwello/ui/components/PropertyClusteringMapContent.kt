package com.example.dwello.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.dwello.data.Property
import com.example.dwello.data.PropertyClusterItem
import com.example.dwello.viewmodel.PropertyViewModel
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun PropertyClusteringMapContent(
    properties: List<Property>,
) {
    // Create a list of PropertyClusterItems from your list of properties
    val propertyClusterItems by remember(properties) {
        mutableStateOf(
            properties.map { property ->
                PropertyClusterItem(property = property)
            }
        )
    }

    // Use the Clustering composable to add the items to the map
    Clustering(
        items = propertyClusterItems,
        onClusterItemClick = {
            // Returning false to let the map handle the default behavior (zoom in on the cluster)
            false
        },
        onClusterClick = {
            // Returning false to let the map handle the default behavior (zoom in on the cluster)
            false
        }
    )
}