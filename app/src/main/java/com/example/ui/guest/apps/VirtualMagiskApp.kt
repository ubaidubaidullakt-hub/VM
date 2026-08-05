package com.example.ui.guest.apps

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VirtualMagiskApp(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechCardBg)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Magisk Title Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(EmeraldRootGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = EmeraldRootGreen)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Magisk v24.3 Manager",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SlateTextPrimary
                )
                Text(
                    text = "Systemless Root Core (Zygisk Enabled)",
                    fontSize = 12.sp,
                    color = EmeraldRootGreen
                )
            }
        }

        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EmeraldRootGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Magisk Core Status", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                    StatusBadge(text = "INSTALLED", badgeColor = EmeraldRootGreen)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Installed Version:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("v24.3 (24300)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Zygisk Framework:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("Yes (Active)", fontSize = 12.sp, color = EmeraldRootGreen)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SU Binary:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("/system/xbin/su", fontSize = 12.sp, color = CyanAccent)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("RAMDisk:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text("Yes (Virtual Root)", fontSize = 12.sp, color = SlateTextPrimary)
                }
            }
        }

        // Modules Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Extension, contentDescription = null, tint = CyanAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Active Magisk Modules (4)", fontWeight = FontWeight.Bold, color = SlateTextPrimary)
                }

                Spacer(modifier = Modifier.height(12.dp))

                ModuleItem("Systemless Hosts (AdBlocker)", "v1.0 • Built-in")
                ModuleItem("LSPosed Framework (Zygisk)", "v1.8.6 • Xposed Modules")
                ModuleItem("Universal SafetyNet Fix", "v2.4.0 • Play Integrity Spoof")
                ModuleItem("Busybox for Android NDK", "v1.34.1 • Root Utils")
            }
        }
    }
}

@Composable
private fun ModuleItem(name: String, details: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldRootGreen, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
            Text(text = details, fontSize = 11.sp, color = SlateTextMuted)
        }
    }
}
