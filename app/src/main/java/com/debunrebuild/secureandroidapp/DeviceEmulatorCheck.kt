package com.debunrebuild.secureandroidapp

import android.os.Build
import java.util.Locale


fun isEmulator(): Boolean {
    return doesHardwareSuggestEmultaor()
            || doesModelSuggestEmulator()
            || doesProductSuggestEmulator()
            || doesFingerPrintSuggestEmulator()
}

private fun doesHardwareSuggestEmultaor(): Boolean {
    return Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.HARDWARE.contains("vbox86")
            || Build.HARDWARE.lowercase(Locale.getDefault()).contains("nox")
}

private fun doesModelSuggestEmulator(): Boolean {
    return Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MODEL.contains("sdk_gphone_x86_64")
            || Build.MODEL.contains("sdk_gphone")
            || Build.MODEL.lowercase(Locale.getDefault()).contains("droid4x")


}

private fun doesProductSuggestEmulator(): Boolean {
    return Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT == "google_sdk"
            || Build.PRODUCT.contains("sdk_gphone_x86")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator")
            || Build.PRODUCT.lowercase(Locale.getDefault()).contains("nox")

}

private fun doesFingerPrintSuggestEmulator(): Boolean {
    return Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
}