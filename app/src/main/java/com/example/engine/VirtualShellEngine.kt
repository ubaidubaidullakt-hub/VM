package com.example.engine

data class ShellOutput(
    val command: String,
    val rawResult: String,
    val exitCode: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

class VirtualShellEngine {

    private val commandHistory = mutableListOf<ShellOutput>()

    fun getHistory(): List<ShellOutput> = commandHistory.toList()

    fun executeCommand(commandInput: String, isRootShell: Boolean = true): ShellOutput {
        val cmd = commandInput.trim()
        val resultText = when {
            cmd == "su" || cmd.startsWith("su ") -> {
                "Root user shell granted (uid=0 root).\n# "
            }
            cmd == "id" || cmd == "whoami" -> {
                if (isRootShell) "uid=0(root) gid=0(root) groups=0(root) context=u:r:su:s0"
                else "uid=10084(u0_a84) gid=10084(u0_a84) groups=10084(u0_a84)"
            }
            cmd == "uname -a" -> {
                "Linux droidvm-guest 4.14.180-aarch64 #1 SMP PREEMPT Android 9 Pie API 28 aarch64"
            }
            cmd == "getprop ro.build.version.release" -> {
                "9.0"
            }
            cmd == "getprop ro.product.cpu.abi" -> {
                "arm64-v8a"
            }
            cmd.startsWith("ls") -> {
                """
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 app
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 bin
                drwxr-xr-x 1 root root  2048 2026-08-05 05:30 dev
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 etc
                -rwxr-xr-x 1 root root 82048 2026-08-05 05:30 init
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 proc
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 sbin
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 sdcard
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 sys
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 system
                drwxr-xr-x 1 root root  4096 2026-08-05 05:30 xbin
                """.trimIndent()
            }
            cmd.startsWith("mount") -> {
                """
                /dev/block/bootdevice/by-name/system on /system type ext4 (rw,seclabel,relatime,data=ordered)
                /dev/block/bootdevice/by-name/userdata on /data type ext4 (rw,seclabel,nosuid,nodev,noatime)
                tmpfs on /storage type tmpfs (rw,seclabel,nosuid,nodev,relatime,mode=0755,gid=1000)
                """.trimIndent()
            }
            cmd == "dmesg | head -n 10" || cmd == "dmesg" -> {
                """
                [    0.000000] Linux version 4.14.180 (droidvm@buildhost) (aarch64-linux-android-gcc) #1 SMP
                [    0.000000] Booting Linux on physical CPU 0x0 [0x410fd034]
                [    0.000000] DroidVM Hypervisor 64-bit Memory Init: 3072MB RAM initialized
                [    0.124500] Selinux: Permissive mode initialized
                [    0.284100] Magisk: Su binary bound to /system/xbin/su
                [    0.412000] Android 9.0 Pie Zygote64 starting...
                """.trimIndent()
            }
            cmd == "ps -A" || cmd == "ps" -> {
                """
                USER     PID   PPID  VSZ   RSS   WCHAN            ADDR S NAME
                root     1     0     1024  512   0          00000000 S init
                root     124   1     2048  812   0          00000000 S magiskd
                system   412   1     45120 12040 0          00000000 S system_server
                u0_a84   1204  412   128400 38200 0         00000000 S com.topjohnwu.magisk
                u0_a85   1892  412   210500 52100 0         00000000 S com.joeykrim.rootcheck
                """.trimIndent()
            }
            cmd == "help" -> {
                "Built-in DroidVM root shell utilities:\nsu, id, whoami, uname -a, getprop, ls, mount, dmesg, ps, clear, reboot"
            }
            cmd == "clear" -> {
                commandHistory.clear()
                return ShellOutput("clear", "")
            }
            else -> {
                "root@droidvm-guest:# $cmd: command executed successfully (exit 0)."
            }
        }

        val output = ShellOutput(cmd, resultText, 0)
        commandHistory.add(output)
        return output
    }
}
