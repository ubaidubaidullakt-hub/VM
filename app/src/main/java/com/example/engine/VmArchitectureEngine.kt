package com.example.engine

import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class VmState {
    STOPPED,
    BOOTING,
    RUNNING,
    PAUSED,
    SHUTTING_DOWN
}

data class HostHardwareInfo(
    val primaryAbi: String,
    val is64BitHost: Boolean,
    val hostModel: String,
    val availableCores: Int,
    val totalRamMb: Int,
    val osVersion: String
)

data class GuestVmInfo(
    val guestAbi: String = "arm64-v8a", // Strictly 64-bit guest
    val guestOsName: String = "Android 9.0 Pie (API 28)",
    val isBinaryTranslationActive: Boolean,
    val translationMode: String,
    val targetArchitecture: String = "aarch64 (ARMv8-A 64-bit)"
)

data class VmTelemetry(
    val state: VmState = VmState.STOPPED,
    val cpuUsagePercent: Float = 0f,
    val ramUsedMb: Int = 0,
    val ramTotalMb: Int = 3072,
    val currentFps: Int = 0,
    val translationMips: Int = 0, // Million Instructions Per Second
    val bootProgress: Float = 0f, // 0.0 to 1.0
    val uptimeSeconds: Long = 0L,
    val activeGuestApp: String = "System Launcher"
)

class VmArchitectureEngine {

    val hostInfo: HostHardwareInfo = detectHostHardware()

    private val _telemetry = MutableStateFlow(
        VmTelemetry(
            state = VmState.STOPPED,
            ramTotalMb = 3072
        )
    )
    val telemetry: StateFlow<VmTelemetry> = _telemetry.asStateFlow()

    private val _guestInfo = MutableStateFlow(
        GuestVmInfo(
            isBinaryTranslationActive = !hostInfo.is64BitHost,
            translationMode = if (!hostInfo.is64BitHost) "Active (32-bit Host -> 64-bit Guest JIT Bridge)" else "Native 64-bit Passthrough"
        )
    )
    val guestInfo: StateFlow<GuestVmInfo> = _guestInfo.asStateFlow()

    private fun detectHostHardware(): HostHardwareInfo {
        val abiList = Build.SUPPORTED_ABIS
        val primaryAbi = abiList.firstOrNull() ?: "arm64-v8a"
        val is64Bit = Build.SUPPORTED_64_BIT_ABIS.isNotEmpty() || primaryAbi.contains("64")
        val cores = Runtime.getRuntime().availableProcessors()
        val totalRam = (Runtime.getRuntime().maxMemory() / (1024 * 1024)).toInt().coerceAtLeast(2048)

        return HostHardwareInfo(
            primaryAbi = primaryAbi,
            is64BitHost = is64Bit,
            hostModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            availableCores = cores,
            totalRamMb = totalRam,
            osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
        )
    }

    fun updateBootProgress(progress: Float, currentStatus: String) {
        val currentState = if (progress >= 1.0f) VmState.RUNNING else VmState.BOOTING
        _telemetry.value = _telemetry.value.copy(
            state = currentState,
            bootProgress = progress,
            activeGuestApp = currentStatus
        )
    }

    fun setVmState(newState: VmState) {
        val updatedTelemetry = when (newState) {
            VmState.STOPPED -> VmTelemetry(state = VmState.STOPPED, ramTotalMb = _telemetry.value.ramTotalMb)
            VmState.RUNNING -> _telemetry.value.copy(state = VmState.RUNNING, currentFps = 60, cpuUsagePercent = 24.5f, ramUsedMb = 1240)
            VmState.PAUSED -> _telemetry.value.copy(state = VmState.PAUSED, currentFps = 0, cpuUsagePercent = 2.0f)
            else -> _telemetry.value.copy(state = newState)
        }
        _telemetry.value = updatedTelemetry
    }

    fun updateMetrics(cpuPercent: Float, ramMb: Int, fps: Int, mips: Int, uptimeSec: Long, currentApp: String) {
        if (_telemetry.value.state == VmState.RUNNING) {
            _telemetry.value = _telemetry.value.copy(
                cpuUsagePercent = cpuPercent,
                ramUsedMb = ramMb,
                currentFps = fps,
                translationMips = mips,
                uptimeSeconds = uptimeSec,
                activeGuestApp = currentApp
            )
        }
    }
}
