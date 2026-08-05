package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.guest.VirtualAndroidScreen
import com.example.ui.guest.VirtualOsViewModel
import com.example.ui.main.MainConsoleScreen
import com.example.ui.main.MainViewModel
import com.example.ui.theme.DroidVmTheme

enum class AppScreen {
    MAIN_HOST_CONSOLE,
    VIRTUAL_ANDROID_OS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DroidVmTheme {
                DroidVmApp()
            }
        }
    }
}

@Composable
fun DroidVmApp() {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.Factory(
        androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
    ))
    val virtualOsViewModel: VirtualOsViewModel = viewModel(factory = VirtualOsViewModel.Factory(
        androidx.compose.ui.platform.LocalContext.current.applicationContext as android.app.Application
    ))

    var currentScreen by remember { mutableStateOf(AppScreen.MAIN_HOST_CONSOLE) }

    when (currentScreen) {
        AppScreen.MAIN_HOST_CONSOLE -> {
            MainConsoleScreen(
                viewModel = mainViewModel,
                onEnterVirtualOs = { currentScreen = AppScreen.VIRTUAL_ANDROID_OS },
                modifier = Modifier.fillMaxSize()
            )
        }
        AppScreen.VIRTUAL_ANDROID_OS -> {
            VirtualAndroidScreen(
                viewModel = virtualOsViewModel,
                onReturnToHostConsole = { currentScreen = AppScreen.MAIN_HOST_CONSOLE },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
