package com.example.ui.guest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.VmDatabase
import com.example.data.local.entities.VirtualAppEntity
import com.example.data.repository.VmRepository
import com.example.engine.RootAccessManager
import com.example.engine.ShellOutput
import com.example.engine.VirtualShellEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class GuestAppWindow {
    LAUNCHER,
    MAGISK_MANAGER,
    ROOT_CHECKER,
    TERMINAL_EMULATOR,
    SETTINGS,
    FILE_EXPLORER,
    RUNNING_CLONED_APP
}

data class RootVerificationResult(
    val isRootGranted: Boolean = true,
    val suPath: String = "/system/xbin/su",
    val busyboxPath: String = "/system/xbin/busybox",
    val architecture: String = "arm64-v8a (64-bit)",
    val selinuxMode: String = "Permissive",
    val magiskVersion: String = "Magisk v24.3 (Zygisk Active)",
    val checkTimestamp: Long = System.currentTimeMillis()
)

class VirtualOsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VmRepository(VmDatabase.getInstance(application))
    val shellEngine = VirtualShellEngine()
    val rootAccessManager = RootAccessManager()

    val installedApps: StateFlow<List<VirtualAppEntity>> = repository.allApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentWindow = MutableStateFlow(GuestAppWindow.LAUNCHER)
    val currentWindow: StateFlow<GuestAppWindow> = _currentWindow.asStateFlow()

    private val _selectedClonedApp = MutableStateFlow<VirtualAppEntity?>(null)
    val selectedClonedApp: StateFlow<VirtualAppEntity?> = _selectedClonedApp.asStateFlow()

    private val _shellHistory = MutableStateFlow<List<ShellOutput>>(emptyList())
    val shellHistory: StateFlow<List<ShellOutput>> = _shellHistory.asStateFlow()

    private val _rootVerification = MutableStateFlow(RootVerificationResult())
    val rootVerification: StateFlow<RootVerificationResult> = _rootVerification.asStateFlow()

    private val _isOverlayMenuExpanded = MutableStateFlow(false)
    val isOverlayMenuExpanded: StateFlow<Boolean> = _isOverlayMenuExpanded.asStateFlow()

    private val _recentAppsList = MutableStateFlow<List<GuestAppWindow>>(listOf(GuestAppWindow.LAUNCHER))
    val recentAppsList: StateFlow<List<GuestAppWindow>> = _recentAppsList.asStateFlow()

    private val _isRecentAppsOpen = MutableStateFlow(false)
    val isRecentAppsOpen: StateFlow<Boolean> = _isRecentAppsOpen.asStateFlow()

    private val _statusNotificationText = MutableStateFlow<String?>(null)
    val statusNotificationText: StateFlow<String?> = _statusNotificationText.asStateFlow()

    init {
        // Execute initial diagnostic command in shell
        executeTerminalCommand("id")
    }

    fun openApp(window: GuestAppWindow, clonedApp: VirtualAppEntity? = null) {
        _currentWindow.value = window
        _selectedClonedApp.value = clonedApp
        _isRecentAppsOpen.value = false
        val currentList = _recentAppsList.value.toMutableList()
        if (!currentList.contains(window)) {
            currentList.add(0, window)
        }
        _recentAppsList.value = currentList
    }

    fun pressHomeButton() {
        _currentWindow.value = GuestAppWindow.LAUNCHER
        _selectedClonedApp.value = null
        _isRecentAppsOpen.value = false
    }

    fun pressBackButton() {
        if (_isRecentAppsOpen.value) {
            _isRecentAppsOpen.value = false
        } else if (_currentWindow.value != GuestAppWindow.LAUNCHER) {
            _currentWindow.value = GuestAppWindow.LAUNCHER
            _selectedClonedApp.value = null
        }
    }

    fun pressRecentButton() {
        _isRecentAppsOpen.value = !_isRecentAppsOpen.value
    }

    fun toggleOverlayMenu() {
        _isOverlayMenuExpanded.value = !_isOverlayMenuExpanded.value
    }

    fun executeTerminalCommand(cmd: String) {
        val output = shellEngine.executeCommand(cmd, isRootShell = true)
        _shellHistory.value = shellEngine.getHistory()

        viewModelScope.launch {
            repository.logRootCommand(
                packageName = "jackpal.androidterm",
                appName = "Root Terminal Emulator",
                command = cmd,
                action = "EXECUTED"
            )
        }
    }

    fun runRootVerificationCheck() {
        viewModelScope.launch {
            _statusNotificationText.value = "Running Root Diagnostic Verification..."
            kotlinx.coroutines.delay(600)
            _rootVerification.value = RootVerificationResult(
                isRootGranted = true,
                suPath = "/system/xbin/su",
                busyboxPath = "/system/xbin/busybox",
                architecture = "arm64-v8a (Strict 64-bit)",
                selinuxMode = "Permissive",
                magiskVersion = "Magisk v24.3 (Zygisk Active)",
                checkTimestamp = System.currentTimeMillis()
            )
            _statusNotificationText.value = "Root verification complete: Pre-installed Root Active!"
        }
    }

    fun clearMemoryAndRecentApps() {
        _recentAppsList.value = listOf(GuestAppWindow.LAUNCHER)
        _isRecentAppsOpen.value = false
        _statusNotificationText.value = "VM Guest Memory Cleaned! Released 480MB RAM."
    }

    fun clearNotification() {
        _statusNotificationText.value = null
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VirtualOsViewModel(application) as T
        }
    }
}
