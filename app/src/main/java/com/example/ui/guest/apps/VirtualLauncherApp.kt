package com.example.ui.guest.apps

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.VirtualAppEntity
import com.example.ui.guest.GuestAppWindow
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VirtualLauncherApp(
    installedApps: List<VirtualAppEntity>,
    onOpenWindow: (GuestAppWindow, VirtualAppEntity?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Android 9 Wallpaper Background
        Image(
            painter = painterResource(id = R.drawable.img_vm_wallpaper_1785933178680),
            contentDescription = "Android 9 OS Wallpaper",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Desktop Grid Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Android 9 Pie Desktop Widget
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.45f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "10:42 AM",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                    Text(
                        text = "Wednesday, August 5 • Android 9.0 (aarch64)",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(EmeraldRootGreen.copy(alpha = 0.25f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldRootGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Root Access Active • su /system/xbin/su",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldRootGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Pre-installed Root & System App Shortcuts Grid
            Text(
                text = "SYSTEM & ROOT APPS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LauncherShortcutItem(
                    title = "Magisk v24.3",
                    icon = Icons.Default.Shield,
                    iconBg = EmeraldRootGreen,
                    testTag = "app_shortcut_magisk",
                    onClick = { onOpenWindow(GuestAppWindow.MAGISK_MANAGER, null) }
                )
                LauncherShortcutItem(
                    title = "Root Checker",
                    icon = Icons.Default.CheckCircle,
                    iconBg = CyanAccent,
                    testTag = "app_shortcut_rootcheck",
                    onClick = { onOpenWindow(GuestAppWindow.ROOT_CHECKER, null) }
                )
                LauncherShortcutItem(
                    title = "Root Terminal",
                    icon = Icons.Default.Terminal,
                    iconBg = Color(0xFF8B5CF6),
                    testTag = "app_shortcut_terminal",
                    onClick = { onOpenWindow(GuestAppWindow.TERMINAL_EMULATOR, null) }
                )
                LauncherShortcutItem(
                    title = "File Explorer",
                    icon = Icons.Default.Folder,
                    iconBg = Color(0xFFF59E0B),
                    testTag = "app_shortcut_explorer",
                    onClick = { onOpenWindow(GuestAppWindow.FILE_EXPLORER, null) }
                )
                LauncherShortcutItem(
                    title = "Settings",
                    icon = Icons.Default.Settings,
                    iconBg = Color(0xFF64748B),
                    testTag = "app_shortcut_settings",
                    onClick = { onOpenWindow(GuestAppWindow.SETTINGS, null) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User / Cloned Apps Grid
            Text(
                text = "CLONED 64-BIT APPS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )

            val clonedUserApps = installedApps.filter { !it.isSystemApp }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(clonedUserApps, key = { it.packageName }) { app ->
                    LauncherShortcutItem(
                        title = app.appName,
                        icon = Icons.Default.Android,
                        iconBg = CyanAccent,
                        testTag = "app_shortcut_${app.packageName}",
                        onClick = { onOpenWindow(GuestAppWindow.RUNNING_CLONED_APP, app) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LauncherShortcutItem(
    title: String,
    icon: ImageVector,
    iconBg: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .testTag(testTag)
            .clickable { onClick() }
            .width(68.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(iconBg.copy(alpha = 0.9f))
                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
