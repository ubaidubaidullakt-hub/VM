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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.VmProfileEntity
import com.example.engine.HostHardwareInfo
import com.example.engine.TranslationMetrics
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VmSettingsTab(
    hostInfo: HostHardwareInfo,
    translationMetrics: TranslationMetrics,
    vmProfile: VmProfileEntity?,
    onSaveSettings: (
        cpuCores: Int,
        ramMb: Int,
        selinuxMode: String,
        renderer: String,
        width: Int,
        height: Int,
        dpi: Int
    ) -> Unit,
    onSpoofImei: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProfile = vmProfile ?: VmProfileEntity()

    var cpuCores by remember(currentProfile) { mutableIntStateOf(currentProfile.cpuCores) }
    var ramSizeMb by remember(currentProfile) { mutableIntStateOf(currentProfile.ramSizeMb) }
    var selinuxMode by remember(currentProfile) { mutableStateOf(currentProfile.selinuxMode) }
    var gpuRenderer by remember(currentProfile) { mutableStateOf(currentProfile.gpuRendererMode) }
    var resolutionWidth by remember(currentProfile) { mutableIntStateOf(currentProfile.resolutionWidth) }
    var resolutionHeight by remember(currentProfile) { mutableIntStateOf(currentProfile.resolutionHeight) }
    var densityDpi by remember(currentProfile) { mutableIntStateOf(currentProfile.densityDpi) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Architecture Compatibility Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Architecture & Bitness Engine",
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent,
                        fontSize = 15.sp
                    )
                    StatusBadge(
                        text = if (hostInfo.is64BitHost) "Physical 64-bit Host" else "Physical 32-bit Host",
                        badgeColor = if (hostInfo.is64BitHost) EmeraldRootGreen else CyanAccent
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Physical Device: ${hostInfo.hostModel} (${hostInfo.primaryAbi})",
                    fontSize = 12.sp,
                    color = SlateTextPrimary
                )
                Text(
                    text = "Internal VM Architecture: arm64-v8a (Strictly 64-bit Android 9 OS)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldRootGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TechCardBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Instruction Translator Status:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateTextSecondary
                        )
                        Text(
                            text = translationMetrics.translationEngineName,
                            fontSize = 12.sp,
                            color = SlateTextPrimary
                        )
                        Text(
                            text = translationMetrics.statusText,
                            fontSize = 11.sp,
                            color = CyanAccent
                        )
                    }
                }
            }
        }

        // Hardware Allocation Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Hardware Core Virtualization",
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Virtual CPU Cores: $cpuCores Cores",
                    fontSize = 13.sp,
                    color = SlateTextSecondary
                )
                Slider(
                    value = cpuCores.toFloat(),
                    onValueChange = { cpuCores = it.toInt() },
                    valueRange = 1f..8f,
                    steps = 6,
                    colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent),
                    modifier = Modifier.testTag("slider_cpu_cores")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Virtual RAM Allocation: $ramSizeMb MB",
                    fontSize = 13.sp,
                    color = SlateTextSecondary
                )
                Slider(
                    value = ramSizeMb.toFloat(),
                    onValueChange = { ramSizeMb = (it / 512).toInt() * 512 },
                    valueRange = 1024f..8192f,
                    colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent),
                    modifier = Modifier.testTag("slider_ram_size")
                )
            }
        }

        // Display & Graphics Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Display & GPU Renderer",
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            resolutionWidth = 720
                            resolutionHeight = 1280
                            densityDpi = 320
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (resolutionWidth == 720) CyanAccent else TechCardBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("720p HD", fontSize = 11.sp, color = if (resolutionWidth == 720) Color.Black else SlateTextPrimary)
                    }

                    Button(
                        onClick = {
                            resolutionWidth = 1080
                            resolutionHeight = 1920
                            densityDpi = 420
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (resolutionWidth == 1080) CyanAccent else TechCardBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1080p FHD", fontSize = 11.sp, color = if (resolutionWidth == 1080) Color.Black else SlateTextPrimary)
                    }

                    Button(
                        onClick = {
                            resolutionWidth = 1440
                            resolutionHeight = 2560
                            densityDpi = 560
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = if (resolutionWidth == 1440) CyanAccent else TechCardBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("1440p QHD", fontSize = 11.sp, color = if (resolutionWidth == 1440) Color.Black else SlateTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Resolution: ${resolutionWidth}x${resolutionHeight} px @ ${densityDpi} DPI",
                    fontSize = 12.sp,
                    color = SlateTextMuted
                )
            }
        }

        // Device Identity & IMEI Spoof Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Device Identity & IMEI Spoofer",
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        fontSize = 15.sp
                    )
                    Button(
                        onClick = onSpoofImei,
                        colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("button_spoof_imei")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Randomize IMEI", fontSize = 11.sp, color = CyanAccent)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Spoofed IMEI: ${currentProfile.imeiSpoof}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = SlateTextPrimary
                )
                Text(
                    text = "Device Model: ${currentProfile.deviceModelSpoof}",
                    fontSize = 12.sp,
                    color = SlateTextSecondary
                )
                Text(
                    text = "Virtual MAC: ${currentProfile.macAddressSpoof}",
                    fontSize = 12.sp,
                    color = SlateTextMuted
                )
            }
        }

        // Save Button
        Button(
            onClick = {
                onSaveSettings(cpuCores, ramSizeMb, selinuxMode, gpuRenderer, resolutionWidth, resolutionHeight, densityDpi)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("button_save_vm_settings"),
            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Apply & Save VM Configuration", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
