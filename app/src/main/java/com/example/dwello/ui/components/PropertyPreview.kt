package com.example.dwello.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dwello.R
import com.example.dwello.data.Property
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyPreview(property: Property, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Added clickable modifier
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column (
            modifier = Modifier.background(Color.White)
        ) {
            val image: Painter = painterResource(id = R.drawable.placeholder_image) // Use the PNG drawable
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                contentScale = ContentScale.Crop
            )
            HomeDetails(property)
        }
    }
}

@Composable
fun HomeDetails(property: Property) {
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
                text = formattedPrice, // Replace with dynamic price
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                fontSize = 24.sp
            )
            IconButton(
                onClick = { /* Save property logic */ },
                modifier = Modifier.size(24.dp)) {
            }
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite",
                tint = Color(0xFFC91818)
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
                append(" sqft")},
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(0.dp))
        Text(
            text = "${property.street}, ${property.city}, ${property.state}",
            style = MaterialTheme.typography.bodyMedium.copy(            color = Color.Black,
                fontSize = 16.sp),
        )
    }
}

@Preview (showBackground = true)
@Composable
fun PropertyPreviewPreview() {
    PropertyPreview(
        property = Property(
            pid = "1",
            price = 23018313,
            beds = 3,
            baths = 2.5,
            sqft = 1500,
            street = "123 Main St",
            city = "Sample City",
            state = "CA",
            zipcode = "12345"
        ),
        onClick = {}
    )
}