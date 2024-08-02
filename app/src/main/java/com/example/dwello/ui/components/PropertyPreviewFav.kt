package com.example.dwello.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dwello.data.Property
import com.example.dwello.R
import com.example.dwello.ui.theme.*
import com.example.dwello.viewmodel.PropertyViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyPreviewFav(
    propertyViewModel: PropertyViewModel,
    property: Property,
    onClick: () -> Unit) {

    val context = LocalContext.current
    val isFavourited by propertyViewModel.isPropertyFavourited(property.pid).collectAsState()

    Box(
        modifier = Modifier
            .padding(2.dp, 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                PropertyPhotoFav(property)
                PropertyDetailsFav(property, context)
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
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(66.dp)
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .padding(0.dp)
                    .background(TransparentWhite, shape = RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFavourited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavourited) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavourited) Red100 else Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun PropertyPhotoFav(property: Property) {
    ImageSlider(property.image_urls, 200)
}

@Composable
fun PropertyDetailsFav(property: Property, context: Context) {
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
                text = formattedPrice, // Dynamic price display
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
                fontSize = 16.sp
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Use the RequestTourButton composable here
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(0.dp)
        ) {
            RequestTourButton(
                phoneNumber = property.phone_number,
                modifier = Modifier.padding(0.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PropertyPreviewFavPreview() {
    val mockViewModel = mockPropertyViewModel()

    PropertyPreviewFav(
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
            thumbnail_url = "https://via.placeholder.com/600x300"
        ),
        onClick = {}
    )
}