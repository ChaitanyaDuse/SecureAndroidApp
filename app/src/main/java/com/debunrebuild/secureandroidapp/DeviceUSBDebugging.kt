package com.debunrebuild.secureandroidapp

import android.content.Context
import android.provider.Settings

/**
 * return true if debugging enabled else return false
 * requires context to fetch the Settings
 */
fun isUsbDebuggingEnabled(context: Context): Boolean {
    return (Settings.Secure.getInt(
        context.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
        0
    ) == 1)
}