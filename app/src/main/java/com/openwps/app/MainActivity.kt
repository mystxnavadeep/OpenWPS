package com.openwps.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openwps.native.jni.NativeBridge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = "OpenWPS", version = NativeBridge.getEngineVersion())
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, version: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!\nEngine: $version",
        modifier = modifier
    )
}
