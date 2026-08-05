package com.example.ui.guest.apps

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontFamily
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

data class VirtualFileNode(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val permissions: String,
    val owner: String,
    val sizeText: String
)

@Composable
fun VirtualFileExplorerApp(modifier: Modifier = Modifier) {
    var currentDirectory by remember { mutableStateOf("/") }

    val filesInRoot = listOf(
        VirtualFileNode("system", "/system", true, "drwxr-xr-x", "root:root", "4096 B"),
        VirtualFileNode("data", "/data", true, "drwxrwx--x", "system:system", "4096 B"),
        VirtualFileNode("sdcard", "/sdcard", true, "drwxrwx--x", "media_rw", "4096 B"),
        VirtualFileNode("init.rc", "/init.rc", false, "-rwxr-xr-x", "root:root", "18.4 KB"),
        VirtualFileNode("default.prop", "/default.prop", false, "-rw-r--r--", "root:root", "1.2 KB")
    )

    val filesInSystem = listOf(
        VirtualFileNode("bin", "/system/bin", true, "drwxr-xr-x", "root:shell", "4096 B"),
        VirtualFileNode("xbin", "/system/xbin", true, "drwxr-xr-x", "root:shell", "4096 B"),
        VirtualFileNode("lib64", "/system/lib64", true, "drwxr-xr-x", "root:root", "8192 B"),
        VirtualFileNode("build.prop", "/system/build.prop", false, "-rw-r--r--", "root:root", "4.8 KB")
    )

    val filesInXbin = listOf(
        VirtualFileNode("su", "/system/xbin/su", false, "-rwsr-sr-x (ROOT)", "root:root", "142 KB"),
        VirtualFileNode("busybox", "/system/xbin/busybox", false, "-rwxr-xr-x", "root:root", "1.8 MB"),
        VirtualFileNode("magisk", "/system/xbin/magisk", false, "-rwxr-xr-x", "root:root", "820 KB")
    )

    val currentFiles = when (currentDirectory) {
        "/system" -> filesInSystem
        "/system/xbin" -> filesInXbin
        else -> filesInRoot
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TechCardBg)
            .padding(12.dp)
    ) {
        // Explorer Path Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(TechCardBorder)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = CyanAccent)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Root Filesystem: $currentDirectory",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = SlateTextPrimary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Breadcrumb Quick Jump Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            BreadcrumbButton("/") { currentDirectory = "/" }
            BreadcrumbButton("/system") { currentDirectory = "/system" }
            BreadcrumbButton("/system/xbin (SU)") { currentDirectory = "/system/xbin" }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // File List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(currentFiles) { file ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (file.isDirectory) {
                                currentDirectory = file.path
                            }
                        }
                        .border(1.dp, TechCardBorder, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = TechCardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (file.name == "su") EmeraldRootGreen.copy(alpha = 0.2f)
                                    else if (file.isDirectory) CyanAccent.copy(alpha = 0.2f)
                                    else TechCardBorder
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (file.name == "su") Icons.Default.Shield else if (file.isDirectory) Icons.Default.Folder else Icons.Default.Description,
                                contentDescription = null,
                                tint = if (file.name == "su") EmeraldRootGreen else if (file.isDirectory) CyanAccent else SlateTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = file.name,
                                fontWeight = FontWeight.Bold,
                                color = if (file.name == "su") EmeraldRootGreen else SlateTextPrimary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${file.permissions} • ${file.owner} • ${file.sizeText}",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = SlateTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreadcrumbButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TechCardBorder)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 11.sp, color = CyanAccent)
    }
}
