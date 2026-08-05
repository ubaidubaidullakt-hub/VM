package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "virtual_apps")
data class VirtualAppEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val version: String,
    val isSystemApp: Boolean = false,
    val isRootGranted: Boolean = false,
    val isRunning: Boolean = false,
    val isFrozen: Boolean = false,
    val installTimestamp: Long = System.currentTimeMillis(),
    val memoryUsageMb: Int = 45,
    val targetAbi: String = "arm64-v8a", // Strictly 64-bit guest ABI
    val iconCategory: String = "TOOL" // SYSTEM, TOOL, GAME, MEDIA, USER
)
