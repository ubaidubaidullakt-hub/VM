package com.example.ui.guest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatusBadge
import com.example.ui.components.VirtualOverlayWheel
import com.example.ui.guest.apps.VirtualFileExplorerApp
import com.example.ui.guest.apps.VirtualLauncherApp
import com.example.ui.guest.apps.VirtualMagiskApp
import com.example.ui.guest.apps.VirtualRootCheckerApp
import com.example.ui.guest.apps.VirtualSettingsApp
import com.example.ui.guest.apps.VirtualTerminalApp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.VirtualOsDesktopBg
import com.example.ui.theme.VirtualOsTaskbar

@Composable
fun VirtualAndroidScreen(
    viewModel: VirtualOsViewModel,
    onReturnToHostConsole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentWindow by viewModel.currentWindow.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    val selectedClonedApp by viewModel.selectedClonedApp.collectAsState()
    val shellHistory by viewModel.shellHistory.collectAsState()
    val rootVerification by viewModel.rootVerification.collectAsState()
    val isOverlayExpanded by viewModel.isOverlayMenuExpanded.collectAsState()
    val recentAppsList by viewModel.recentAppsList.collectAsState()
    val isRecentOpen by viewModel.isRecentAppsOpen.collectAsState()
    val statusNotificationText by viewModel.statusNotificationText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusNotificationText) {
        statusNotificationText?.let { text ->
            snackbarHostState.showSnackbar(text)
            viewModel.clearNotification()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = VirtualOsDesktopBg,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Android 9 System Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .background(VirtualOsTaskbar)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Pre-installed Root",
                            tint = EmeraldRootGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "ROOT ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldRootGreen
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusBadge(text = "aarch64 (64-bit)", badgeColor = CyanAccent)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Wifi, contentDescription = "Virtual Wifi", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.BatteryFull, contentDescription = "Battery", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "10:42", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Guest App Display Canvas Frame
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    when (currentWindow) {
                        GuestAppWindow.LAUNCHER -> VirtualLauncherApp(
                            installedApps = installedApps,
                            onOpenWindow = { window, clonedApp -> viewModel.openApp(window, clonedApp) }
                        )
                        GuestAppWindow.MAGISK_MANAGER -> VirtualMagiskApp()
                        GuestAppWindow.ROOT_CHECKER -> VirtualRootCheckerApp(
                            verification = rootVerification,
                            onVerifyClick = { viewModel.runRootVerificationCheck() }
                        )
                        GuestAppWindow.TERMINAL_EMULATOR -> VirtualTerminalApp(
                            history = shellHistory,
                            onExecuteCommand = { cmd -> viewModel.executeTerminalCommand(cmd) }
                        )
                        GuestAppWindow.SETTINGS -> VirtualSettingsApp()
                        GuestAppWindow.FILE_EXPLORER -> VirtualFileExplorerApp()
                        GuestAppWindow.RUNNING_CLONED_APP -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(TechCardBg)
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Android,
                                        contentDescription = null,
                                        tint = CyanAccent,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = selectedClonedApp?.appName ?: "Cloned App",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SlateTextPrimary
                                    )
                                    Text(
                                        text = "Running inside 64-bit Guest VM Environment",
                                        fontSize = 13.sp,
                                        color = SlateTextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    StatusBadge(text = "Root Granted", badgeColor = EmeraldRootGreen)
                                }
                            }
                        }
                    }

                    // Android 9 Recent Apps Switcher Overlay
                    androidx.compose.animation.AnimatedVisibility(visible = isRecentOpen) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.85f))
                                .padding(20.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Android 9 Recent Apps",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Button(
                                        onClick = { viewModel.clearMemoryAndRecentApps() },
                                        colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("button_clear_vm_ram")
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Clear All RAM", fontSize = 12.sp, color = CyanAccent)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(recentAppsList) { window ->
                                        Card(
                                            modifier = Modifier
                                                .size(width = 200.dp, height = 340.dp)
                                                .clickable { viewModel.openApp(window) }
                                                .border(1.dp, CyanAccent, RoundedCornerShape(16.dp)),
                                            colors = CardDefaults.cardColors(containerColor = TechCardBg)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(16.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Android,
                                                    contentDescription = null,
                                                    tint = CyanAccent,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Text(
                                                    text = window.name,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SlateTextPrimary,
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Android 9 System Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(VirtualOsTaskbar),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.pressBackButton() },
                        modifier = Modifier.testTag("nav_vm_back")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    IconButton(
                        onClick = { viewModel.pressHomeButton() },
                        modifier = Modifier.testTag("nav_vm_home")
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }

                    IconButton(
                        onClick = { viewModel.pressRecentButton() },
                        modifier = Modifier.testTag("nav_vm_recents")
                    ) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Recent Apps", tint = Color.White)
                    }
                }
            }

            // Floating Overlay Quick Tools Wheel
            VirtualOverlayWheel(
                isExpanded = isOverlayExpanded,
                onToggleExpand = { viewModel.toggleOverlayMenu() },
                onReturnToHostConsole = onReturnToHostConsole,
                onOpenTerminal = {
                    viewModel.openApp(GuestAppWindow.TERMINAL_EMULATOR)
                    viewModel.toggleOverlayMenu()
                },
                onTakeScreenshot = {
                    viewModel.runRootVerificationCheck()
                    viewModel.toggleOverlayMenu()
                },
                onShakeDevice = { viewModel.toggleOverlayMenu() },
                onRotateScreen = { viewModel.toggleOverlayMenu() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 64.dp)
            )
        }
    }
}
