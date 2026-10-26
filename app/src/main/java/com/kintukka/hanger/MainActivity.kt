package com.kintukka.hanger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kintukka.hanger.ui.navigation.HangerNavHost
import com.kintukka.hanger.ui.theme.HangerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            HangerTheme {
                HangerNavHost()
            }
        }
    }
}
