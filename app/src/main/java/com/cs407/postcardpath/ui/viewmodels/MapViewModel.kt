package com.cs407.postcardpath.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapState(
// A list of markers currently displayed on the map
    val markers: List<LatLng> = emptyList(),
// Stores the user's most recent location (if available)
    val currentLocation: LatLng? = null,
// Tracks whether location permissions are granted
    val locationPermissionGranted: Boolean = false,
// Indicates when location or map data is being loaded
    val isLoading: Boolean = false,
// Stores any error message encountered
    val error: String? = null
)

class MapViewModel : ViewModel() {
    // Backing property (private) for state: MutableStateFlow allows us
// to update data internally
    private val _uiState = MutableStateFlow(MapState())
    // Publicly exposed immutable StateFlow for the UI layer to observe changes safely
    val uiState = _uiState.asStateFlow()
    // FusedLocationProviderClient interacts with Android's location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    // Initializes the location client when a valid Context becomes available
    fun initializeLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
    // Coroutine function to fetch the user's current location
    fun getCurrentLocation() {
        viewModelScope.launch {
// TODO: 1 - Set isLoading to true and clear previous errors
// TODO: 2 - Retrieve the last known location using fusedLocationClient
// TODO: 3 - Handle cases where location is null (set an appropriate error message)
// TODO: 4 - If successful, update currentLocation with latitude and longitude
// TODO: 5 - Always set isLoading back to false when done
// TODO: 6 - Wrap logic inside try-catch to handle possible exceptions
        }
    }
    // Updates permission flag when the user grants or denies location access
    fun updateLocationPermission(granted: Boolean) {
        _uiState.value = _uiState.value.copy(locationPermissionGranted = granted)
    }
}