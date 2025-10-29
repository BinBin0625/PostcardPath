package com.cs407.postcardpath.ui.screens

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@Composable
fun TakePhoto() {

    val context = LocalContext.current

    // State to hold the URI of the captured image
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // State to hold a temporary URI while the camera is active
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    val captureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            Toast.makeText(context, "Image capture: ${if(success) "Successful" else "Failed"}", Toast.LENGTH_SHORT)
                .show()
            if (success) {
                imageUri = tempUri
            }
        }
    )

    fun launchCamera() {
        // Permission is already granted, launch camera
        val imageFile = createImageFile(context)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName!!}.fileprovider", // Authority must match AndroidManifest
            imageFile
        )
        tempUri = uri // Save the uri for the camera result
        captureLauncher.launch(uri)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission granted, create a new URI and launch the camera
                launchCamera()
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
                            launchCamera()
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

fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    // Create the temporary image file
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