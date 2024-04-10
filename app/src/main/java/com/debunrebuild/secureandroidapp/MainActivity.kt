package com.debunrebuild.secureandroidapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            startActivity(intent);
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.screen_lock_status)?.let {
            it.text = getScreenLockEnabledString(isDeviceScreenLocked(applicationContext))
        }
    }

    fun getScreenLockEnabledString(isEnabled: Boolean): String {
        val isEnabledStr = if (isEnabled) "Enabled" else "Disabled"
        return String.format(getString(R.string.sscreen_lock_status), isEnabledStr)
    }
}