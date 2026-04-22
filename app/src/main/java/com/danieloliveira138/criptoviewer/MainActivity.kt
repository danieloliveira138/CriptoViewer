package com.danieloliveira138.criptoviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.danieloliveira138.criptoviewer.presentation.navigation.AppNavigation
import com.danieloliveira138.criptoviewer.ui.theme.CryptoViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoViewerTheme(darkTheme = true, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
