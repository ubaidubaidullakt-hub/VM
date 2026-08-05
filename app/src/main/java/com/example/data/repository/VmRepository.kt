package com.example.data.repository

import com.example.data.local.VmDatabase
import com.example.data.local.entities.RootLogEntity
import com.example.data.local.entities.VirtualAppEntity
import com.example.data.local.entities.VmProfileEntity
import kotlinx.coroutines.flow.Flow

class VmRepository(private val db: VmDatabase) {
    val allApps: Flow<List<VirtualAppEntity>> = db.virtualAppDao().getAllApps()
    val rootGrantedApps: Flow<List<VirtualAppEntity>> = db.virtualAppDao().getRootGrantedApps()
    val defaultProfile: Flow<VmProfileEntity?> = db.vmProfileDao().getProfileFlow("vm_default_64bit")
    val allProfiles: Flow<List<VmProfileEntity>> = db.vmProfileDao().getAllProfiles()
    val recentRootLogs: Flow<List<RootLogEntity>> = db.rootLogDao().getRecentLogs()

    suspend fun initializeSeedDataIfNeeded() {
        if (db.vmProfileDao().getProfile("vm_default_64bit") == null) {
            db.vmProfileDao().insertProfile(
                VmProfileEntity(
                    instanceId = "vm_default_64bit",
                    instanceName = "Android 9.0 (aarch64) Rooted",
                    guestOsVersion = "Android 9.0 Pie (API 28)",
                    guestAbi = "arm64-v8a (64-bit)",
                    cpuCores = 4,
                    ramSizeMb = 3072,
                    isRootEnabled = true,
                    selinuxMode = "Permissive"
                )
            )
        }

        // Pre-installed Virtual System Apps inside Android 9 VM
        val initialApps = listOf(
            VirtualAppEntity(
                packageName = "com.topjohnwu.magisk",
                appName = "Magisk v24.3 Manager",
                version = "24.3 (24300)",
                isSystemApp = true,
                isRootGranted = true,
                targetAbi = "arm64-v8a",
                iconCategory = "SYSTEM"
            ),
            VirtualAppEntity(
                packageName = "com.joeykrim.rootcheck",
                appName = "Root Checker Pro",
                version = "6.5.2",
                isSystemApp = true,
                isRootGranted = true,
                targetAbi = "arm64-v8a",
                iconCategory = "TOOL"
            ),
            VirtualAppEntity(
                packageName = "jackpal.androidterm",
                appName = "Root Terminal Emulator",
                version = "1.0.70",
                isSystemApp = true,
                isRootGranted = true,
                targetAbi = "arm64-v8a",
                iconCategory = "TOOL"
            ),
            VirtualAppEntity(
                packageName = "com.android.settings",
                appName = "Virtual Settings (Android 9)",
                version = "9.0.0-Pie",
                isSystemApp = true,
                isRootGranted = false,
                targetAbi = "arm64-v8a",
                iconCategory = "SYSTEM"
            ),
            VirtualAppEntity(
                packageName = "com.android.documentsui",
                appName = "Virtual File Explorer",
                version = "9.0.0",
                isSystemApp = true,
                isRootGranted = true,
                targetAbi = "arm64-v8a",
                iconCategory = "SYSTEM"
            ),
            VirtualAppEntity(
                packageName = "com.example.cloned.game",
                appName = "CyberRacer 3D (64-bit)",
                version = "2.1.0",
                isSystemApp = false,
                isRootGranted = false,
                targetAbi = "arm64-v8a",
                iconCategory = "GAME"
            ),
            VirtualAppEntity(
                packageName = "com.example.cloned.social",
                appName = "SocialConnect Cloner",
                version = "4.12.0",
                isSystemApp = false,
                isRootGranted = false,
                targetAbi = "arm64-v8a",
                iconCategory = "USER"
            )
        )

        for (app in initialApps) {
            if (db.virtualAppDao().getAppByPackage(app.packageName) == null) {
                db.virtualAppDao().insertApp(app)
            }
        }
    }

    suspend fun saveProfile(profile: VmProfileEntity) {
        db.vmProfileDao().insertProfile(profile)
    }

    suspend fun toggleRootAccess(packageName: String, granted: Boolean, appName: String) {
        db.virtualAppDao().updateRootAccess(packageName, granted)
        val action = if (granted) "GRANTED" else "REVOKED"
        db.rootLogDao().insertLog(
            RootLogEntity(
                requestingPackage = packageName,
                appName = appName,
                commandExecuted = "su -c 'access_check'",
                actionTaken = action
            )
        )
    }

    suspend fun installApp(app: VirtualAppEntity) {
        db.virtualAppDao().insertApp(app)
    }

    suspend fun uninstallApp(packageName: String) {
        db.virtualAppDao().deleteApp(packageName)
    }

    suspend fun logRootCommand(packageName: String, appName: String, command: String, action: String) {
        db.rootLogDao().insertLog(
            RootLogEntity(
                requestingPackage = packageName,
                appName = appName,
                commandExecuted = command,
                actionTaken = action
            )
        )
    }

    suspend fun clearLogs() {
        db.rootLogDao().clearLogs()
    }
}
