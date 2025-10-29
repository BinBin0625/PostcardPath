package com.cs407.postcardpath.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TakePhoto() {

    val context = LocalContext.current

    // State to hold the URI of the captured image
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // State to hold a temporary URI while the camera is active
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Define the launcher for the camera
    val captureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        // What to do once the capture is complete, either successfully or cancelled
        onResult = { success ->
            Toast.makeText(context, "Photo taken: ${if(success) "Successful" else "Failed"}", Toast.LENGTH_SHORT)
                .show()
            if (success) {
                // Save the temp URI as the final image URI
                imageUri = tempUri
                // TODO actually do something with it
            } else {
                // Photo capture failed or was cancelled.
                // Delete the empty file we created.
                tempUri?.let { uri ->
                    context.contentResolver.delete(uri, null, null)
                }
            }
        }
    )

    // Launch the camera with a destination file URI
    fun prepareImageFileAndLaunchCamera() {
        // Create a new file
        val imageFile = createImageFile(context)
        // Get the URI for the file
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Authority must match AndroidManifest
            imageFile
        )
        // Save the uri for the camera result
        tempUri = uri
        // Start the capture session with the URI
        captureLauncher.launch(uri)
    }

    // Launch the permission request with a callback for what to do on user response
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, create a new URI and launch the camera
                prepareImageFileAndLaunchCamera()
            } else {
                // Permission denied. Show a message or handle accordingly.
                // TODO
            }
        })

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(

            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = null,
            modifier = Modifier
                .weight(0.7f),
        )

        Column(
            modifier = Modifier
                .weight(0.3f),
        ) {
            Button(

                onClick = {
                    // Check if permission is already granted
                    when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                        PackageManager.PERMISSION_GRANTED -> {
                            // Permission is already granted, launch camera
                            prepareImageFileAndLaunchCamera()
                        }
                        else -> {
                            // Permission not granted, request it
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                }
            ) {
                Text("Take Photo")
            }
        }


    }
}

// Create an image file in the app's storage directory
private fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    // Create the image file
    // Use createTempFile to guarantee a unique filename. The actual file is not temporary.
    val imageFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )

    return imageFile
}



@Preview(showBackground = true)
@Composable
fun PreviewTakePhoto() {
    TakePhoto()
}