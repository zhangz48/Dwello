package com.example.dwello.ui

import android.util.Log
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dwello.R
import com.example.dwello.data.Property
import com.example.dwello.ui.components.ImageSlider
import com.example.dwello.ui.components.RequestTourButton
import com.example.dwello.ui.components.handlePhoneCall
import com.example.dwello.ui.components.makePhoneCall
import com.example.dwello.ui.components.mockPropertyViewModel
import com.example.dwello.ui.theme.DwelloTheme
import com.example.dwello.ui.theme.*
import com.example.dwello.viewmodel.PropertyViewModel
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun PropertyListingScreen(
    navController: NavController,
    property: Property,
    propertyViewModel: PropertyViewModel
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { offset ->
                Log.d("PropertyListingScreen", "Scroll offset: $offset")
            }
    }

    val isFavourited by propertyViewModel.isPropertyFavourited(property.pid).collectAsState()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }
    val formattedPrice = currencyFormat.format(property.price)
    val formattedMonthly = currencyFormat.format(property.est_monthly)
    val formattedPricePerSqft = currencyFormat.format(property.price / property.sqft)
    val formattedHOA = currencyFormat.format(property.hoa)
    val formattedSqft = NumberFormat.getNumberInstance(Locale.US).format(property.sqft)
    val formattedDays =
        NumberFormat.getNumberInstance(Locale.US).format(property.list_date.toDate().daysAgo())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 80.dp) // Padding to prevent content from being obscured by the button
        ) {
            item {
                // Display the image slider with swipe functionality
                ImageSlider(property.image_urls, 300)
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(Color.Gray)
                        .align(Alignment.TopCenter)
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    // Price and Details Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = formattedPrice,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("${property.beds}")
                                    }
                                    append(" bds    ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("${property.baths}")
                                    }
                                    append(" ba    ")

                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(formattedSqft)
                                    }
                                    append(" sqft")
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${property.street}, ${property.city}, ${property.state} ${property.zipcode}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp
                                )
                            )
                        }

                        // Map Icon
                        Image(
                            painter = painterResource(id = R.drawable.map2),
                            contentDescription = "Map",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estimated Monthly Cost
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .background(color = BackgroundBlue, shape = RoundedCornerShape(8.dp))
                            .padding(0.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.padding(14.dp, 0.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Est. ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(formattedMonthly)
                                    }
                                    append("/mo")
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 16.sp
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .padding(14.dp, 0.dp)
                        ) {
                            val context = LocalContext.current
                            var showDialog by remember { mutableStateOf(false) }

                            val callPermissionLauncher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.RequestPermission(),
                                onResult = { isGranted ->
                                    if (isGranted) {
                                        makePhoneCall(context, property.phone_number)
                                    } else {
                                        Toast.makeText(context, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )

                            TextButton(
                                onClick = {
                                    showDialog = true
                                },
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .padding(0.dp)
                                    .defaultMinSize(
                                        minWidth = 1.dp,
                                        minHeight = 1.dp
                                    )  // Remove any minimum size constraints
                            ) {
                                Text(
                                    text = "Learn More",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = ButtonBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                )
                            }

                            handlePhoneCall(
                                context = context,
                                phoneNumber = property.phone_number,
                                callPermissionLauncher = { callPermissionLauncher.launch(Manifest.permission.CALL_PHONE) },
                                showDialog = showDialog,
                                setShowDialog = { showDialog = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Property Details
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PropertyDetailsRow(label = "Price/Sq. Ft.", value = formattedPricePerSqft)
                    PropertyDetailsRow(label = "On Dwello", value = "$formattedDays days")
                    PropertyDetailsRow(label = "Built", value = "${property.built_year}")
                    PropertyDetailsRow(label = "HOA", value = formattedHOA)
                    PropertyDetailsRow(label = "Property Type", value = property.property_type)
                    PropertyDetailsRow(
                        label = "Parking",
                        value = "${property.parking_space} garage spaces"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // About this Home
                    Text(
                        text = "About this home",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = property.about_home,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        // Fixed "Request a Tour" Button at the Bottom with Overlap
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.White)
        ) {
            // Top border line
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.Gray)
                    .align(Alignment.TopCenter)
            )
            RequestTourButton(
                phoneNumber = property.phone_number,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Top Bar
        val backgroundColor by remember {
            derivedStateOf {
                if (listState.firstVisibleItemScrollOffset > 5) Color.White else Color.Transparent
            }
        }
        val contentColor by remember {
            derivedStateOf {
                if (listState.firstVisibleItemScrollOffset > 5) Color.Black else Color.White
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(0.dp, 0.dp)
                .background(backgroundColor)
                .align(Alignment.TopCenter)
                .zIndex(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp, 6.dp, 14.dp, 0.dp)
                    .height(54.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        Log.d("PropertyListingScreen", "Back button clicked")
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .padding(0.dp)
                        .background(TransparentWhite, shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = contentColor,
                        modifier = Modifier.size(34.dp)
                    )
                }
                IconButton(
                    onClick = {
                        if (isFavourited) {
                            propertyViewModel.removePropertyFromFavourites(property.pid)
                        } else {
                            propertyViewModel.addPropertyToFavourites(property.pid)
                        }
                        Log.d("PropertyListingScreen", "Favourite button clicked and Favourite status: $isFavourited")
                    },
                    modifier = Modifier
                        .size(34.dp)
                        .padding(0.dp)
                        .background(TransparentWhite, shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = if (isFavourited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavourited) "Remove from favorites" else "Add to favorites",
                        tint = if (isFavourited) Red100 else contentColor,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PropertyDetailsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = DarkGrey,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

// Helper extension function to calculate days ago from Timestamp
fun Date.daysAgo(): Long {
    val currentDate = LocalDate.now()
    val inputDate = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return ChronoUnit.DAYS.between(inputDate, currentDate)
}

@Preview(showBackground = true)
@Composable
fun PropertyListingScreenPreview() {
    DwelloTheme {
        // Creating a fake NavController for preview purposes
        val navController = rememberNavController()
        val mockViewModel = mockPropertyViewModel() // Replace with a real or mock view model

        PropertyListingScreen(
            // Creating a fake NavController for preview purposes
            navController = navController,

            property = Property(
                pid = "1",
                about_home = "Immaculately maintained and Quality Built 2..." +
                        "Immaculately maintained and Quality Built 2..." +
                        "Immaculately maintained and Quality Built 2..." +
                        "Immaculately maintained and Quality Built 2...",
                baths = 6.5,
                beds = 4,
                built_year = 2016,
                city = "Bellevue",
                est_monthly = 42073,
                hoa = 0,
                image_urls = listOf("https://via.placeholder.com/150"),
                list_date = Timestamp.now(),
                parking_space = 4,
                price = 6800000,
                property_type = "Single-family",
                state = "WA",
                street = "1111 NE 28th St",
                sqft = 3710,
                thumbnail_url = "https://firebasestorage.googleapis.com/v0/b/dwello-b3d97.appspot.com/o/property_images%2F1-1.jpg?alt=media&token=68e4bc3e-f019-4949-81af-0c9785c4d956",
                zipcode = "98008"
            ),

            mockViewModel
        )
    }
}