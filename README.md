# V-Droid Engine 📱⚡

An Android virtualized OS container app built with modern Jetpack Compose and Kotlin, supporting root shell access simulation, multi-app switching, x86_64 architecture translation, and system configuration controls.

---

## 🚀 How to Export to GitHub & Download the APK

### Option 1: Direct APK Download & GitHub Push from AI Studio
1. **Push to GitHub**: Click the **Settings / Share / Export** icon in the upper right corner of Google AI Studio and select **"Push to GitHub"** to export this entire repository directly to your GitHub account.
2. **Download APK**: You can also select **"Generate APK / Export APK"** directly from the AI Studio menu to download the compiled `.apk` file to your computer or phone.

---

### Option 2: Automated APK Download on GitHub (GitHub Actions)
This repository includes a pre-configured GitHub Actions workflow in `.github/workflows/build-apk.yml`.

1. Once pushed to GitHub, go to the **Actions** tab in your GitHub repository.
2. Select the latest **"Build & Release Android APK"** run.
3. Scroll down to the **Artifacts** section and download **`V-Droid-Engine-APK`**.
4. Unzip the downloaded file to get your installable `app-debug.apk` file!

---

## ✨ Features
- **Virtual Android 9 Interface**: Android 9 pie-style navigation, status bar, and recent apps switcher.
- **Root Console & Magisk Engine**: Simulates su binary access, root shell, SELinux policies, and permission grants.
- **Multi-App Switching**: Instant recent apps overlay for quick toggling between virtual applications.
- **System Architecture Visualizer**: Live x86_64 / ARM64 architecture stats and memory metrics.
