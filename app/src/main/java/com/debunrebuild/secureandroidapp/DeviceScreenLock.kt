package com.debunrebuild.secureandroidapp

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Returns whether the device is secured with a PIN, pattern or
 * password.
 *
 * <p>See also {@link #isKeyguardSecure} which treats SIM locked states as secure.
 *
 * @return {@code true} if a PIN, pattern or password was set.
 */
fun isDeviceScreenLocked(context: Context): Boolean {
    val keyGuardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
    return keyGuardManager?.isDeviceSecure ?: false
}

/**
 *
 * If need to redirect the user only to the security tab on the settings screen
 * ACTION_SECURITY_SETTINGS
 */
fun getIntentToScreenLock(): Intent {
    return Intent(Settings.ACTION_SECURITY_SETTINGS)
}