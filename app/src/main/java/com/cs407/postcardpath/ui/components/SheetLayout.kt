package com.cs407.postcardpath.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetLayout(navController: NavController, sheetState: SheetState) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var startingPath by remember { mutableStateOf(false) }

    if (!startingPath) {


        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            Text(
                text = "Create Path",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(0.5f)
                ) {


                    Button(
                        onClick = {
                            navController.navigate("takephoto")

                            scope.launch {
                                withContext(Dispatchers.Main) {
                                    sheetState.hide()
                                }
                            }

                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Add, "Add Icon")
                    }

                    Text(
                        text = "Take a Photo",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )

                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(0.5f)
                ) {

                    Button(
                        onClick = {
                            startingPath = true
                        },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, "Play Icon")
                    }

                    Text(
                        text = "Start a Path",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                    )

                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Ready?",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT)
                        .show()
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            sheetState.hide()
                        }
                    }

                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PlayArrow, "Play Icon")
                    Text("Go!")
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewCreateScreen() {
    SheetLayout(navController = rememberNavController(), sheetState = rememberModalBottomSheetState())
}