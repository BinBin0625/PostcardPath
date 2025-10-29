package com.cs407.postcardpath.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cs407.postcardpath.ui.viewmodels.MapViewModel

@Composable
fun CreateScreen(
    navController: NavController,
    viewModel: MapViewModel = viewModel()
) {
    val context = LocalContext.current

    // Observe the current UI state from the ViewModel.
    // This automatically updates the UI whenever data changes.
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // TODO: Ask for location permission if not already granted.
    // [HINT] You can use a helper composable like LocationPermissionHelper
    // and call viewModel.updateLocationPermission(granted) when permission is approved.
    // Define a default (fallback) location, for example the UW-Madison campus.
    // This ensures that the map loads to a valid position even before
    // the user's actual location is obtained.
    val defaultLocation = LatLng(43.0731, -89.4012) // Madison, WI
    // Create a camera state to control and remember the map's current viewpoint.
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }
    // Automatically move (animate) the camera when the location changes.
    // LaunchedEffect runs a coroutine whenever uiState.currentLocation updates.
    LaunchedEffect(uiState.currentLocation) {
        uiState.currentLocation?.let { location ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                location, 15f // 15f = street-level zoom
            )
        }
    }

    // Defines a state object that controls the camera's position on the map
    val initLocation = LatLng(1.35, 103.87) // Hardcoded coordinates (Singapore)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

    }
}



@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    CreateScreen(navController = rememberNavController())
}