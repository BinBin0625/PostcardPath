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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cs407.postcardpath.ui.theme.primary
import com.cs407.postcardpath.ui.theme.PurpleGrey80
import com.cs407.postcardpath.ui.theme.PurpleGrey40

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("paths", Icons.Default.LocationOn, "Paths"),
        BottomNavItem("create", Icons.Default.Add, "Create"),
        BottomNavItem("settings", Icons.Default.Settings, "Settings")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        tonalElevation = 8.dp,
        containerColor = PurpleGrey80
    ) {
        items.forEach { item ->
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
    }
}
