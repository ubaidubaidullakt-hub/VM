package com.example.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.VmDatabase
import com.example.data.local.entities.RootLogEntity
import com.example.data.local.entities.VirtualAppEntity
import com.example.data.local.entities.VmProfileEntity
import com.example.data.repository.VmRepository
import com.example.engine.BinaryTranslationBridge
import com.example.engine.HostHardwareInfo
import com.example.engine.RootAccessManager
import com.example.engine.TranslationMetrics
import com.example.engine.VirtualHardwareConfig
import com.example.engine.VirtualHardwareManager
import com.example.engine.VmArchitectureEngine
import com.example.engine.VmState
import com.example.engine.VmTelemetry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VmRepository(VmDatabase.getInstance(application))
    val vmArchEngine = VmArchitectureEngine()
    val binaryTranslationBridge = BinaryTranslationBridge(
        isHost64Bit = vmArchEngine.hostInfo.is64BitHost,
        hostAbi = vmArchEngine.hostInfo.primaryAbi
    )
    val rootAccessManager = RootAccessManager()
    val hardwareManager = VirtualHardwareManager()

    val hostInfo: HostHardwareInfo = vmArchEngine.hostInfo
    val telemetry: StateFlow<VmTelemetry> = vmArchEngine.telemetry
    val translationMetrics: TranslationMetrics = binaryTranslationBridge.getTranslationMetrics()

    val installedApps: StateFlow<List<VirtualAppEntity>> = repository.allApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vmProfile: StateFlow<VmProfileEntity?> = repository.defaultProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allVmProfiles: StateFlow<List<VmProfileEntity>> = repository.allProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rootLogs: StateFlow<List<RootLogEntity>> = repository.recentRootLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeTab = MutableStateFlow(0) // 0: App Manager, 1: Settings, 2: Root Console, 3: Multi-Instance
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
            startTelemetryMonitor()
        }
    }

    fun selectTab(index: Int) {
        _activeTab.value = index
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun startVm() {
        viewModelScope.launch {
            if (telemetry.value.state == VmState.RUNNING || telemetry.value.state == VmState.BOOTING) return@launch

            vmArchEngine.updateBootProgress(0.1f, "Initializing Hypervisor Kernel...")
            delay(400)
            vmArchEngine.updateBootProgress(0.35f, "Configuring 64-bit ARM64 Instruction Memory...")
            delay(400)
            vmArchEngine.updateBootProgress(0.65f, "Injecting Preinstalled Root su & Magisk v24.3...")
            delay(500)
            vmArchEngine.updateBootProgress(0.90f, "Starting Android 9 Pie Zygote64...")
            delay(400)
            vmArchEngine.updateBootProgress(1.0f, "Android 9 OS Ready")
            _toastMessage.value = "Virtual Machine booted successfully (64-bit Android 9)"
        }
    }

    fun stopVm() {
        viewModelScope.launch {
            vmArchEngine.setVmState(VmState.STOPPED)
            _toastMessage.value = "Virtual Machine shut down"
        }
    }

    fun pauseVm() {
        viewModelScope.launch {
            if (telemetry.value.state == VmState.RUNNING) {
                vmArchEngine.setVmState(VmState.PAUSED)
                _toastMessage.value = "Virtual Machine paused"
            } else if (telemetry.value.state == VmState.PAUSED) {
                vmArchEngine.setVmState(VmState.RUNNING)
                _toastMessage.value = "Virtual Machine resumed"
            }
        }
    }

    fun toggleRootPermission(packageName: String, currentStatus: Boolean, appName: String) {
        viewModelScope.launch {
            repository.toggleRootAccess(packageName, !currentStatus, appName)
            _toastMessage.value = if (!currentStatus) "Root granted to $appName" else "Root revoked from $appName"
        }
    }

    fun importHostAppToVm(appName: String, packageName: String, category: String) {
        viewModelScope.launch {
            val newApp = VirtualAppEntity(
                packageName = packageName,
                appName = appName,
                version = "1.0.0-cloned",
                isSystemApp = false,
                isRootGranted = false,
                targetAbi = "arm64-v8a",
                iconCategory = category
            )
            repository.installApp(newApp)
            _toastMessage.value = "Imported $appName into Virtual Android 9"
        }
    }

    fun updateHardwareSettings(
        cpuCores: Int,
        ramMb: Int,
        selinuxMode: String,
        renderer: String,
        resolutionWidth: Int,
        resolutionHeight: Int,
        dpi: Int
    ) {
        viewModelScope.launch {
            val current = vmProfile.value ?: VmProfileEntity()
            val updated = current.copy(
                cpuCores = cpuCores,
                ramSizeMb = ramMb,
                selinuxMode = selinuxMode,
                gpuRendererMode = renderer,
                resolutionWidth = resolutionWidth,
                resolutionHeight = resolutionHeight,
                densityDpi = dpi
            )
            repository.saveProfile(updated)
            rootAccessManager.setSelinuxMode(selinuxMode)
            _toastMessage.value = "VM Configuration saved"
        }
    }

    fun generateNewImei() {
        viewModelScope.launch {
            val newImei = hardwareManager.generateNewImei()
            val current = vmProfile.value ?: VmProfileEntity()
            repository.saveProfile(current.copy(imeiSpoof = newImei))
            _toastMessage.value = "Spoofed IMEI: $newImei"
        }
    }

    fun createNewVmInstance(instanceName: String) {
        viewModelScope.launch {
            val newId = "vm_inst_${System.currentTimeMillis()}"
            val newProfile = VmProfileEntity(
                instanceId = newId,
                instanceName = instanceName,
                guestOsVersion = "Android 9.0 Pie (API 28)",
                guestAbi = "arm64-v8a (64-bit)"
            )
            repository.saveProfile(newProfile)
            _toastMessage.value = "Created VM Instance: $instanceName"
        }
    }

    fun clearRootLogs() {
        viewModelScope.launch {
            repository.clearLogs()
            _toastMessage.value = "Root logs cleared"
        }
    }

    private fun startTelemetryMonitor() {
        viewModelScope.launch {
            var uptime = 0L
            while (true) {
                delay(1000)
                if (telemetry.value.state == VmState.RUNNING) {
                    uptime++
                    val cpu = (15..48).random().toFloat()
                    val ram = (1100..2400).random()
                    val fps = (58..60).random()
                    val mips = if (hostInfo.is64BitHost) 0 else (170..190).random()
                    val apps = listOf("Pixel Launcher", "Magisk Manager", "Terminal Emulator", "Root Checker", "Virtual Settings")
                    val currentApp = apps.random()
                    vmArchEngine.updateMetrics(cpu, ram, fps, mips, uptime, currentApp)
                }
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application) as T
        }
    }
}
