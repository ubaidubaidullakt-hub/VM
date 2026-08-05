package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vm_profiles")
data class VmProfileEntity(
    @PrimaryKey val instanceId: String = "vm_default_64bit",
    val instanceName: String = "Android 9.0 (aarch64) Rooted",
    val guestOsVersion: String = "Android 9.0 Pie (API 28)",
    val guestAbi: String = "arm64-v8a (Strict 64-bit)",
    val cpuCores: Int = 4,
    val ramSizeMb: Int = 3072,
    val resolutionWidth: Int = 1080,
    val resolutionHeight: Int = 1920,
    val densityDpi: Int = 420,
    val isRootEnabled: Boolean = true,
    val selinuxMode: String = "Permissive", // Permissive, Enforcing
    val binaryTranslatorMode: String = "JIT Dual-Bitness Bridge", // Auto, JIT Dual-Bitness, Software Interpreter
    val gpuRendererMode: String = "OpenGL ES 3.2 Passthrough",
    val frameRateCap: Int = 60,
    val imeiSpoof: String = "864209041234567",
    val deviceModelSpoof: String = "Google Pixel 3 XL (Android 9)",
    val macAddressSpoof: String = "02:00:00:1A:2B:3C",
    val isRunning: Boolean = false,
    val activeUptimeMs: Long = 0L,
    val createdTimestamp: Long = System.currentTimeMillis()
)
