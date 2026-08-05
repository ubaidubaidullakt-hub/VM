package com.example.ui.guest.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VirtualSettingsApp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechCardBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = CyanAccent)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Android 9 System Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
        }

        // About Virtual Phone Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = CyanAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "About Virtual Phone", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))

                SettingRow("Device Name", "Google Pixel 3 XL (Virtual)")
                SettingRow("Android Version", "9.0.0 (Pie API 28)")
                SettingRow("Build Number", "PQ3A.190801.002 (DroidVM-Root)")
                SettingRow("Guest Architecture", "arm64-v8a (aarch64)")
                SettingRow("Kernel Version", "4.14.180-droidvm-aarch64")
            }
        }

        // Root & Security Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = EmeraldRootGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Root & System Permissions", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))

                SettingRow("Root Access", "Enabled (/system/xbin/su)")
                SettingRow("SELinux Mode", "Permissive")
                SettingRow("Magisk Core", "v24.3 Active")
                SettingRow("Developer Options", "Enabled (USB Debugging ON)")
            }
        }
    }
}

@Composable
private fun SettingRow(title: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 12.sp, color = SlateTextSecondary)
        Text(text = detail, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
    }
}
