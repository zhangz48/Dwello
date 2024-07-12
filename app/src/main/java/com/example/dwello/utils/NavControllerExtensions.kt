package com.example.dwello.utils

import android.util.Log
import androidx.navigation.NavController

fun NavController.logBackStack() {
    //Log.d("NavControllerBackStack", "Current Back Stack:")
    this.currentBackStackEntry?.let { entry ->
        Log.d("NavControllerBackStack", "Current: ${entry.destination.route ?: "Unknown Route"}")
    }
    this.previousBackStackEntry?.let { entry ->
        Log.d("NavControllerBackStack", "Previous: ${entry.destination.route ?: "Unknown Route"}")
    }
}