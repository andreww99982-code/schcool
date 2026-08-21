package com.schcool.trainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.schcool.trainer.ui.navigation.AppNavHost
import com.schcool.trainer.ui.theme.SchcoolTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SchcoolTheme {
                AppNavHost()
            }
        }
    }
}
