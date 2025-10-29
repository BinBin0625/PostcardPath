package com.cs407.postcardpath.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// This is a funky nested class now, and can store data for NavBar items, or Composables.
// Very limited research showed this as a reasonable way to handle this without having to redefine each nav button.
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
