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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.guest.RootVerificationResult
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldRootGreen
import com.example.ui.theme.SlateTextMuted
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.SlateTextSecondary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VirtualRootCheckerApp(
    verification: RootVerificationResult,
    onVerifyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechCardBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Main Result Banner Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, EmeraldRootGreen, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = EmeraldRootGreen.copy(alpha = 0.15f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(EmeraldRootGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.Black, modifier = Modifier.size(36.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Congratulations! Root Access is properly installed on this device!",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldRootGreen,
                    fontSize = 15.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Internal OS: Android 9.0 Pie • Architecture: ${verification.architecture}",
                    fontSize = 12.sp,
                    color = SlateTextPrimary
                )
            }
        }

        // Diagnostic Breakdown Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TechCardBorder, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "System Diagnostics Breakdown", fontWeight = FontWeight.Bold, color = SlateTextPrimary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))

                DiagnosticRow("su Binary", verification.suPath, true)
                DiagnosticRow("Busybox Binary", verification.busyboxPath, true)
                DiagnosticRow("Guest Architecture", verification.architecture, true)
                DiagnosticRow("SELinux Mode", verification.selinuxMode, true)
                DiagnosticRow("Magisk Framework", verification.magiskVersion, true)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onVerifyClick,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldRootGreen, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("button_verify_root")
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Verify Root Access Again", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun DiagnosticRow(label: String, value: String, isOk: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, fontSize = 12.sp, color = SlateTextSecondary)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SlateTextPrimary)
        }
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isOk) EmeraldRootGreen else SlateTextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}
