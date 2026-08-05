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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.RootLogEntity
import com.example.engine.RootEnvironmentStatus
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RootConsoleTab(
    rootLogs: List<RootLogEntity>,
    onClearLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rootStatus = RootEnvironmentStatus()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Preinstalled Root Environment Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, EmeraldRootGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = TechCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmeraldRootGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = EmeraldRootGreen)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Pre-Installed Root System",
                                fontWeight = FontWeight.Bold,
                                color = SlateTextPrimary,
                                fontSize = 15.sp
                            )
                            Text(
                                text = rootStatus.magiskVersion,
                                fontSize = 12.sp,
                                color = EmeraldRootGreen
                            )
                        }
                    }

                    StatusBadge(text = "SU PRE-INSTALLED", badgeColor = EmeraldRootGreen)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SU Binary Path:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(rootStatus.suBinaryLocation, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = SlateTextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SELinux Status:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(rootStatus.selinuxStatus, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Module Framework:", fontSize = 12.sp, color = SlateTextSecondary)
                    Text(rootStatus.xposedModuleFramework, fontSize = 12.sp, color = SlateTextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Root Access Activity Logs Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Superuser Audit Log (${rootLogs.size})",
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary,
                fontSize = 15.sp
            )

            Button(
                onClick = onClearLogs,
                colors = ButtonDefaults.buttonColors(containerColor = TechCardBorder),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("button_clear_root_logs")
            ) {
                Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clear Audit Logs", fontSize = 12.sp, color = SlateTextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rootLogs, key = { it.id }) { log ->
                RootLogCardItem(log = log)
            }
        }
    }
}

@Composable
private fun RootLogCardItem(log: RootLogEntity) {
    val isGranted = log.actionTaken == "GRANTED" || log.actionTaken == "EXECUTED"
    val badgeColor = if (isGranted) EmeraldRootGreen else RoseError
    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(log.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TechCardBorder, RoundedCornerShape(10.dp)),
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
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.Check else Icons.Default.Lock,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = log.appName,
                        fontWeight = FontWeight.Bold,
                        color = SlateTextPrimary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formattedTime,
                        fontSize = 10.sp,
                        color = SlateTextMuted
                    )
                }
                Text(
                    text = "Command: ${log.commandExecuted}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = SlateTextSecondary
                )
            }

            StatusBadge(text = log.actionTaken, badgeColor = badgeColor)
        }
    }
}
