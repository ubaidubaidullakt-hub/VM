package com.example.engine

data class VirtualHardwareConfig(
    val cpuCores: Int = 4,
    val ramSizeMb: Int = 3072,
    val gpuRenderer: String = "OpenGL ES 3.2 Passthrough",
    val screenWidth: Int = 1080,
    val screenHeight: Int = 1920,
    val screenDpi: Int = 420,
    val imei: String = "864209041234567",
    val modelName: String = "Google Pixel 3 XL (Android 9)",
    val macAddress: String = "02:00:00:1A:2B:3C",
    val androidId: String = "9774d56d682e549c"
)

class VirtualHardwareManager {
    private var config = VirtualHardwareConfig()

    fun getConfig(): VirtualHardwareConfig = config

    fun updateConfig(newConfig: VirtualHardwareConfig) {
        config = newConfig
    }

    fun generateNewImei(): String {
        val randomDigits = (10000000..99999999).random()
        val newImei = "86420904$randomDigits"
        config = config.copy(imei = newImei)
        return newImei
    }

    fun generateNewMacAddress(): String {
        val b1 = String.format("%02X", (0..255).random())
        val b2 = String.format("%02X", (0..255).random())
        val b3 = String.format("%02X", (0..255).random())
        val newMac = "02:00:00:$b1:$b2:$b3"
        config = config.copy(macAddress = newMac)
        return newMac
    }
}
