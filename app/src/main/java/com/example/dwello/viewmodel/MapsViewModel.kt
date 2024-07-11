package com.example.dwello.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dwello.fragments.MapsFragment
import kotlinx.coroutines.launch

class MapsViewModel : ViewModel() {
    val mapsFragment = MapsFragment()
}