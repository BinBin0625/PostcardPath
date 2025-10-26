package com.cs407.postcardpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.cs407.postcardpath.ui.navigation.AppNavigation
import com.cs407.postcardpath.ui.theme.PostcardPathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PostcardPathTheme {
                AppNavigation()
            }
        }
    }
}
