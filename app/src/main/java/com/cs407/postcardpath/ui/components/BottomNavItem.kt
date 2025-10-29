package com.cs407.postcardpath.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem {
    data class BottomNavData(
        val route: String,
        val icon: ImageVector,
        val label: String
    ) : BottomNavItem()
    data class ComposableBottomNavItem(
        val content: @Composable () -> Unit
    ) : BottomNavItem()

}
