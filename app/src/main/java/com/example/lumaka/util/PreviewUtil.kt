package com.example.lumaka.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


/**
 * Provides a NavController suitable for @Preview functions.
 * It won't actually navigate, but allows Composables requiring a NavController
 * to be previewed.
 */
@Composable
fun rememberPreviewNavController(): NavHostController {
    return rememberNavController()
}