package com.example.ui.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.VirtualAppEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootBg
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.RoseError
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun AppManagerTab(
    appsList: List<VirtualAppEntity>,
    onToggleRoot: (packageName: String, currentStatus: Boolean, appName: String) -> Unit,
    onImportApp: (appName: String, packageName: String, category: String) -> Unit,
    onLaunchAppInVm: (VirtualAppEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showImportDialog by remember { mutableStateOf(false) }
    var newAppName by remember { mutableStateOf("") }
    var newPackageName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Virtual Android 9 Apps",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "${appsList.size} Apps Installed inside 64-bit Guest",
                    fontSize = 12.sp,
                    color = SlateTextSecondary
                )
            }

            Button(
                onClick = { showImportDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("button_import_app")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Import APK", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showImportDialog) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .border(1.dp, CyanAccent, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = TechCardBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Clone / Import App to 64-bit VM",
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newAppName,
                        onValueChange = { newAppName = it },
                        label = { Text("App Name", color = SlateTextSecondary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_import_app_name"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = TechCardBorder,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPackageName,
                        onValueChange = { newPackageName = it },
                        label = { Text("Package Name (e.g. com.app.demo)", color = SlateTextSecondary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_import_package_name"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = TechCardBorder,
                            focusedTextColor = SlateTextPrimary,
                            unfocusedTextColor = SlateTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { showImportDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancel", color = SlateTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (newAppName.isNotBlank() && newPackageName.isNotBlank()) {
                                    onImportApp(newAppName, newPackageName, "USER")
                                    newAppName = ""
                                    newPackageName = ""
                                    showImportDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("confirm_import_app")
                        ) {
                            Text("Import APK", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(appsList, key = { it.packageName }) { app ->
                VirtualAppCardItem(
                    app = app,
                    onToggleRoot = { onToggleRoot(app.packageName, app.isRootGranted, app.appName) },
                    onLaunch = { onLaunchAppInVm(app) }
                )
            }
        }
    }
}

@Composable
private fun VirtualAppCardItem(
    app: VirtualAppEntity,
    onToggleRoot: () -> Unit,
    onLaunch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("app_card_${app.packageName}")
            .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = TechCardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (app.isSystemApp) CyanAccent.copy(alpha = 0.2f) else EmeraldRootGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (app.isSystemApp) Icons.Default.Security else Icons.Default.Android,
                    contentDescription = null,
                    tint = if (app.isSystemApp) CyanAccent else EmeraldRootGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = app.appName,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (app.isSystemApp) {
                        StatusBadge(text = "System", badgeColor = CyanAccent)
                    } else {
                        StatusBadge(text = "Cloned", badgeColor = EmeraldRootGreen)
                    }
                }
                Text(
                    text = "${app.packageName} • ABI: ${app.targetAbi}",
                    fontSize = 11.sp,
                    color = SlateTextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (app.isRootGranted) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (app.isRootGranted) EmeraldRootGreen else SlateTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (app.isRootGranted) "Root" else "User",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (app.isRootGranted) EmeraldRootGreen else SlateTextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = app.isRootGranted,
                        onCheckedChange = { onToggleRoot() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = EmeraldRootGreen,
                            checkedTrackColor = EmeraldRootBg,
                            uncheckedThumbColor = SlateTextMuted,
                            uncheckedTrackColor = TechCardBorder
                        ),
                        modifier = Modifier.testTag("switch_root_${app.packageName}")
                    )
                }

                Button(
                    onClick = onLaunch,
                    colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder, contentColor = CyanAccent),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .height(28.dp)
                        .testTag("launch_app_${app.packageName}")
                ) {
                    Text("Run in VM", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
