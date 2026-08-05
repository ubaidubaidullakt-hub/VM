package com.example.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.VirtualAppEntity
import com.example.engine.VmState
import com.example.ui.components.StatMeter
import com.example.ui.components.StatusBadge
import com.example.ui.main.tabs.AppManagerTab
import com.example.ui.main.tabs.RootConsoleTab
import com.example.ui.main.tabs.VmInstancesTab
import com.example.ui.main.tabs.VmSettingsTab
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.RoseError
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechSapphireBg

@Composable
fun MainConsoleScreen(
    viewModel: MainViewModel,
    onEnterVirtualOs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()
    val vmProfile by viewModel.vmProfile.collectAsState()
    val allProfiles by viewModel.allVmProfiles.collectAsState()
    val rootLogs by viewModel.rootLogs.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = TechSapphireBg,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = TechCardBg,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(Icons.Default.Apps, contentDescription = "Apps") },
                    label = { Text("App Manager") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanAccent,
                        indicatorColor = CyanAccent,
                        unselectedIconColor = SlateTextMuted,
                        unselectedTextColor = SlateTextMuted
                    ),
                    modifier = Modifier.testTag("tab_app_manager")
                )

                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("VM Settings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanAccent,
                        indicatorColor = CyanAccent,
                        unselectedIconColor = SlateTextMuted,
                        unselectedTextColor = SlateTextMuted
                    ),
                    modifier = Modifier.testTag("tab_vm_settings")
                )

                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(Icons.Default.Security, contentDescription = "Root") },
                    label = { Text("Root Manager") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanAccent,
                        indicatorColor = CyanAccent,
                        unselectedIconColor = SlateTextMuted,
                        unselectedTextColor = SlateTextMuted
                    ),
                    modifier = Modifier.testTag("tab_root_manager")
                )

                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = { Icon(Icons.Default.DeveloperBoard, contentDescription = "Instances") },
                    label = { Text("Instances") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = CyanAccent,
                        indicatorColor = CyanAccent,
                        unselectedIconColor = SlateTextMuted,
                        unselectedTextColor = SlateTextMuted
                    ),
                    modifier = Modifier.testTag("tab_instances")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Header Panel & VM Power Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = TechCardBg)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.img_vm_icon_1785933164890),
                                contentDescription = "DroidVM Icon",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "DroidVM Engine",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                                Text(
                                    text = "Android 9.0 Pie (arm64-v8a)",
                                    fontSize = 12.sp,
                                    color = SlateTextSecondary
                                )
                            }
                        }

                        StatusBadge(
                            text = when (telemetry.state) {
                                VmState.STOPPED -> "OFFLINE"
                                VmState.BOOTING -> "BOOTING"
                                VmState.RUNNING -> "VM RUNNING"
                                VmState.PAUSED -> "PAUSED"
                                VmState.SHUTTING_DOWN -> "STOPPING"
                            },
                            badgeColor = when (telemetry.state) {
                                VmState.RUNNING -> EmeraldRootGreen
                                VmState.BOOTING -> CyanAccent
                                VmState.PAUSED -> Color(0xFFF59E0B)
                                else -> SlateTextMuted
                            }
                        )
                    }

                    if (telemetry.state == VmState.BOOTING) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = telemetry.activeGuestApp,
                                    fontSize = 11.sp,
                                    color = CyanAccent
                                )
                                Text(
                                    text = "${(telemetry.bootProgress * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SlateTextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { telemetry.bootProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyanAccent,
                                trackColor = TechCardBorder
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // VM Power Control Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (telemetry.state == VmState.STOPPED) {
                            Button(
                                onClick = { viewModel.startVm() },
                                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("button_start_vm")
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Power On VM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        } else {
                            Button(
                                onClick = { onEnterVirtualOs() },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldRootGreen, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("button_enter_virtual_os")
                            ) {
                                Icon(imageVector = Icons.Default.Smartphone, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Open Guest Screen", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = { viewModel.pauseVm() },
                                colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder, contentColor = SlateTextPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("button_pause_vm")
                            ) {
                                Icon(
                                    imageVector = if (telemetry.state == VmState.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Button(
                                onClick = { viewModel.stopVm() },
                                colors = ButtonDefaults.buttonColors(containerColor = RoseError.copy(alpha = 0.2f), contentColor = RoseError),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("button_stop_vm")
                            ) {
                                Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Real-time Hardware Performance Meters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMeter(
                    label = "Virtual CPU",
                    valueText = "${telemetry.cpuUsagePercent.toInt()}%",
                    progress = telemetry.cpuUsagePercent / 100f,
                    barColor = CyanAccent,
                    modifier = Modifier.weight(1f)
                )

                StatMeter(
                    label = "RAM Allocated",
                    valueText = "${telemetry.ramUsedMb} MB",
                    progress = telemetry.ramUsedMb.toFloat() / telemetry.ramTotalMb,
                    barColor = EmeraldRootGreen,
                    modifier = Modifier.weight(1f)
                )

                StatMeter(
                    label = "Guest Display",
                    valueText = "${telemetry.currentFps} FPS",
                    progress = telemetry.currentFps / 60f,
                    barColor = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tab Content Frame
            Box(modifier = Modifier.weight(1f)) {
                when (activeTab) {
                    0 -> AppManagerTab(
                        appsList = installedApps,
                        onToggleRoot = { pkg, status, name -> viewModel.toggleRootPermission(pkg, status, name) },
                        onImportApp = { name, pkg, cat -> viewModel.importHostAppToVm(name, pkg, cat) },
                        onLaunchAppInVm = { app ->
                            viewModel.startVm()
                            onEnterVirtualOs()
                        }
                    )
                    1 -> VmSettingsTab(
                        hostInfo = viewModel.hostInfo,
                        translationMetrics = viewModel.translationMetrics,
                        vmProfile = vmProfile,
                        onSaveSettings = { cores, ram, selinux, renderer, w, h, dpi ->
                            viewModel.updateHardwareSettings(cores, ram, selinux, renderer, w, h, dpi)
                        },
                        onSpoofImei = { viewModel.generateNewImei() }
                    )
                    2 -> RootConsoleTab(
                        rootLogs = rootLogs,
                        onClearLogs = { viewModel.clearRootLogs() }
                    )
                    3 -> VmInstancesTab(
                        profilesList = allProfiles,
                        onCreateInstance = { name -> viewModel.createNewVmInstance(name) },
                        onStartInstance = {
                            viewModel.startVm()
                            onEnterVirtualOs()
                        }
                    )
                }
            }
        }
    }
}
