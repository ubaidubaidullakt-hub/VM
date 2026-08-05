package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.SlateTextPrimary
import com.example.ui.theme.TechCardBg
import com.example.ui.theme.TechCardBorder

@Composable
fun VirtualOverlayWheel(
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onReturnToHostConsole: () -> Unit,
    onOpenTerminal: () -> Unit,
    onTakeScreenshot: () -> Unit,
    onShakeDevice: () -> Unit,
    onRotateScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(TechCardBg.copy(alpha = 0.95f))
                    .border(1.dp, TechCardBorder, RoundedCornerShape(16.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                OverlayMenuItem(
                    icon = Icons.Default.Home,
                    label = "Main Host Console",
                    onClick = onReturnToHostConsole,
                    testTag = "overlay_return_host"
                )
                OverlayMenuItem(
                    icon = Icons.Default.Terminal,
                    label = "Root Terminal",
                    onClick = onOpenTerminal,
                    testTag = "overlay_terminal"
                )
                OverlayMenuItem(
                    icon = Icons.Default.CameraAlt,
                    label = "Capture Screenshot",
                    onClick = onTakeScreenshot,
                    testTag = "overlay_screenshot"
                )
                OverlayMenuItem(
                    icon = Icons.Default.Vibration,
                    label = "Simulate Shake",
                    onClick = onShakeDevice,
                    testTag = "overlay_shake"
                )
                OverlayMenuItem(
                    icon = Icons.Default.ScreenRotation,
                    label = "Rotate Screen",
                    onClick = onRotateScreen,
                    testTag = "overlay_rotate"
                )
            }
        }

        FloatingActionButton(
            onClick = onToggleExpand,
            containerColor = CyanAccent,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .size(52.dp)
                .testTag("floating_vm_wheel")
        ) {
            Icon(
                imageVector = Icons.Default.DeveloperMode,
                contentDescription = "VM Quick Tools Menu"
            )
        }
    }
}

@Composable
private fun OverlayMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = SlateTextPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(TechCardBorder),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = CyanAccent,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
