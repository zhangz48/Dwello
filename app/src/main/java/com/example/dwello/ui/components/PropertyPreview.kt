package com.example.dwello.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dwello.R
import com.example.dwello.data.Property
import com.example.dwello.ui.theme.*
import com.example.dwello.viewmodel.PropertyViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyPreview(
    propertyViewModel: PropertyViewModel,
    property: Property,
    onClick: () -> Unit,
) {
    val isFavourited by propertyViewModel.isPropertyFavourited(property.pid).collectAsState()

    Box(
        modifier = Modifier
            .width(380.dp)
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                PropertyPhoto(property)
                PropertyDetails(property)
            }
        }

        // Heart icon positioned above the card
        IconButton(
            onClick = {
                if (isFavourited) {
                    propertyViewModel.removePropertyFromFavourites(property.pid)
                    Log.d("PropertyPreviewFav", "Removing property ${property.pid} from favourites")
                } else {
                    propertyViewModel.addPropertyToFavourites(property.pid)
                    Log.d("PropertyPreviewFav", "Adding property ${property.pid} to favourites")
                }
                Log.d("PropertyPreviewFav", "Favourite button clicked and Favourite status: $isFavourited")
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp)
                .size(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .padding(0.dp)
                    .background(TransparentWhite, shape = RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavourited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavourited) "Remove from favourites" else "Add to favourites",
                    tint = if (isFavourited) Red100 else Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(3.dp)
                )
            }
        }
    }
}

@Composable
fun PropertyPhoto(property: Property) {
    ImageSlider(property.image_urls, 190)
}

@Composable
fun PropertyDetails(
    property: Property,
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }
    val formattedPrice = currencyFormat.format(property.price)
    val formattedSqft = NumberFormat.getNumberInstance(Locale.US).format(property.sqft)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 2.dp, 12.dp, 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(0.dp, 2.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 24.sp
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("${property.beds}")
                pop()
                append(" bds")
                pushStyle(SpanStyle(color = Color.Gray))
                append("  |  ")
                pop()
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append("${property.baths}")
                pop()
                append(" ba")
                pushStyle(SpanStyle(color = Color.Gray))
                append("  |  ")
                pop()
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(formattedSqft)
                pop()
                append(" sqft")
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = "${property.street}, ${property.city}, ${property.state}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontSize = 16.sp),
        )
    }
}

@Composable
fun mockPropertyViewModel(): PropertyViewModel {
    // This requires that you can instantiate PropertyViewModel with mock dependencies
    return PropertyViewModel(LocalContext.current)
}

@Preview(showBackground = true)
@Composable
fun PropertyPreviewPreview() {
    val mockViewModel = mockPropertyViewModel()

    PropertyPreview(
        propertyViewModel = mockViewModel,
        property = Property(
            pid = "1",
            price = 23018313,
            beds = 3,
            baths = 2.5,
            sqft = 1500,
            street = "123 Main St",
            city = "Sample City",
            state = "CA",
            zipcode = "12345",
            image_urls = listOf("https://via.placeholder.com/600x300") // Sample image URL
        ),
        onClick = {}
    )
}