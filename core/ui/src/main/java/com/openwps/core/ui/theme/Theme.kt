package com.openwps.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun OpenWPSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}
