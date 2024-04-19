package com.debunrebuild.secureandroidapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.debunrebuild.secureandroidapp.R.id.usb_debugging_status_check_btn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.navigate_user_to_settings_screen).setOnClickListener {
            val intent = getIntentToScreenLock()
            startActivity(intent)
        }
        findViewById<TextView>(R.id.device_emulator_status).text = getDeviceEmulatorStatusScreen(
            isEmulator()
        )

        findViewById<TextView>(R.id.usb_debugging_status_text).text = getUSBDebuggingStatusString(
            isUsbDebuggingEnabled(applicationContext)
        )

        findViewById<Button>(usb_debugging_status_check_btn).setOnClickListener {
            findViewById<TextView>(R.id.usb_debugging_status_text).text =
                getUSBDebuggingStatusString(
                    isUsbDebuggingEnabled(applicationContext)
                )
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.screen_lock_status)?.let {
            it.text = getScreenLockEnabledString(isDeviceScreenLocked(applicationContext))
        }
    }

    private fun getScreenLockEnabledString(isEnabled: Boolean): String {
        val isEnabledStr = if (isEnabled) "Enabled" else "Disabled"
        return String.format(getString(R.string.sscreen_lock_status), isEnabledStr)
    }

    private fun getDeviceEmulatorStatusScreen(isEmulator: Boolean): String {
        val deviceEnabledString = if (isEmulator) "is" else "is not"
        return String.format(getString(R.string.emulator_status), deviceEnabledString)
    }

    private fun getUSBDebuggingStatusString(isUSbDebuggingEnabled: Boolean): String {
        return if (isUSbDebuggingEnabled) return "ON" else "OFF"
    }
}