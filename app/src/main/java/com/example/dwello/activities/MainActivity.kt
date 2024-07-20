package com.example.dwello.activities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dwello.ui.theme.DwelloTheme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dwello.BuildConfig
import com.example.dwello.R
import com.example.dwello.utils.logBackStack
import com.example.dwello.viewmodel.AuthViewModel
import com.example.dwello.viewmodel.MapsViewModel
import com.example.dwello.viewmodel.MapsViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

val customFontFamily = FontFamily(
    Font(R.font.cormorant_garamond_regular),
    Font(R.font.cormorant_garamond_bold, FontWeight.Bold),
    Font(R.font.cormorant_garamond_medium, FontWeight.Medium),
    // Add other font variants as needed
)

class MainActivity : FragmentActivity() {

    private lateinit var auth: FirebaseAuth
    private val authViewModel: AuthViewModel by viewModels()
    private val mapsViewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        // Define a variable to hold the Places API key.
        val apiKey = BuildConfig.MAPS_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
            finish()
            return
        }

        // Initialize the SDK
        Places.initialize(applicationContext, apiKey)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

//        // Initialize the AutocompleteSupportFragment.
//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
//                    as AutocompleteSupportFragment
//
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
//
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                // TODO: Get info about the selected place.
//                Log.i(TAG, "Place: ${place.name}, ${place.id}")
//                mapsViewModel.updateSelectedPlace(place)
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i(TAG, "An error occurred: $status")
//            }
//        })

        setContent {
            DwelloTheme {
                MainScreen(
                    authViewModel = authViewModel,
                    mapsViewModel = mapsViewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel, mapsViewModel: MapsViewModel) {
    val navController = rememberNavController()
//    val currentBackStack by navController.currentBackStackEntryAsState()
//    val currentDestination = currentBackStack?.destination
//    val currentScreen = bottomNavigationScreens.find { it.route == currentDestination?.route } ?: Screen.Home
    val isLoggedIn by authViewModel.user.collectAsState()
    var selectedTab by remember { mutableStateOf<Screen>(Screen.Home) }
    var redirectScreen by rememberSaveable { mutableStateOf<Screen?>(null)}

    Log.d("MainScreen", "MainScreen Composable rendered")

    LaunchedEffect(isLoggedIn) {
        redirectScreen?.let { screen ->
            if (isLoggedIn != null) {
                Log.d("MainScreen", "User logged in, redirecting to: ${screen.route}")

                // Attempt to pop the auth screen from the back stack
                val popped = navController.popBackStack(AuthScreen.Auth.route, inclusive = true)
                Log.d("MainScreen", "popBackStack result: $popped")

                // Navigate to the intended screen
                navController.navigateSingleTopTo(screen.route)
                redirectScreen = null

                // Log final back stack state
                navController.logBackStack()
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                allScreens = bottomNavigationScreens,
                onTabSelected = { newScreen ->
                    selectedTab = newScreen
                    if (isLoggedIn != null || newScreen == Screen.Home) {
                        navController.navigateSingleTopTo(newScreen.route)
                    } else {
                        redirectScreen = newScreen
                        Log.d("MainScreen", "User not logged in, redirecting to AuthScreen")
                        navController.navigateSingleTopTo(AuthScreen.Auth.route)
                    }
                },
                currentScreen = selectedTab,
            )
        }
    ) { innerPadding ->
        NavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            //isLoggedIn = isLoggedIn != null,
            authViewModel = authViewModel,
            //redirectScreen = redirectScreen,
            mapsViewModel = mapsViewModel
        )
    }
}