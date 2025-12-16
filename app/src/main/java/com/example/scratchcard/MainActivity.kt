package com.example.scratchcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sampletask.ScratchCardApp
import com.example.scratchcard.ui.theme.ScratchCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScratchCardTheme {
                ScratchCardApp()
            }
        }
    }
}
