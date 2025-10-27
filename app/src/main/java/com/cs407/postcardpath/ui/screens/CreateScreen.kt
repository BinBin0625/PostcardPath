package com.cs407.postcardpath.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs407.postcardpath.Camera
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs407.postcardpath.ui.viewmodels.MapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
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

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(true) }

    // Defines a state object that controls the camera's position on the map
    val initLocation = LatLng(1.35, 103.87) // Hardcoded coordinates (Singapore)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // The GoogleMap composable displays the map UI inside your Compose layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        Button(
            onClick = { showBottomSheet = true }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                SheetLayout()
            }
        }

    }
}

@Composable
fun SheetLayout() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Screen",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, Camera::class.java)
                context.startActivity(intent)
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp)
        ) {
            Text("Open Camera")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    CreateScreen()
}