package com.cs407.postcardpath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.reflect.ComposableMethod
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cs407.postcardpath.ui.theme.primary
import com.cs407.postcardpath.ui.theme.PurpleGrey80
import com.cs407.postcardpath.ui.theme.PurpleGrey40
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation

// This Experimental Opt is silly, but I guess the ModalBottomSheet isn't out of experimental yet
// (I did try updating Compose, but that broke the icons for reasons (see link below) and didn't remove the experimental requirement)
// https://www.linkedin.com/posts/sasikanthmiriyampalli_icons-jetpack-compose-android-developers-activity-7377553673826000897-Y5aq
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // A list of nav buttons and composables to put in the nav bar
    val items = listOf(
        BottomNavItem.BottomNavData("paths", Icons.Default.LocationOn, "Paths"),
        BottomNavItem.ComposableBottomNavItem(
            @Composable
            {
                Button(
                    onClick = {
                        // Return to the paths view
                        if (currentRoute != "paths") {
                            navController.navigate("paths")
                        }
                        scope.launch {
                            withContext(Dispatchers.Main) {
                                sheetState.show()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Route Button",
                    )
                }
            }
        ),
        BottomNavItem.BottomNavData("settings", Icons.Default.Settings, "Settings")
    )

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = PurpleGrey80
    ) {
        items.forEach { item ->
            // Distinguish between the two types of items
            when (item) {
                is BottomNavItem.BottomNavData -> {
                    val selected = currentRoute == item.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (selected) primary else PurpleGrey40
                                )
                                if (selected) {
                                    Spacer(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .height(3.dp)
                                            .width(24.dp)
                                            .background(primary, CircleShape)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(7.dp))
                                }
                            }
                        },
                        label = null
                    )
                }

                is BottomNavItem.ComposableBottomNavItem -> {
                    item.content()
                }
            }
        }
        // Turns out, if you don't remember to create this if wrapper, the sheet will disappear but still steal all your inputs
        // We put the sheet in BottomNavigationBar because that's where the button to show it lives
        if (sheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            sheetState.hide()
                        }
                    }
                },
                sheetState = sheetState,
            ) {
                SheetLayout(navController, sheetState)
            }

        }


    }
}

@Preview
@Composable
fun PreviewBottomNavBar() {
    BottomNavigationBar(rememberNavController())
}
