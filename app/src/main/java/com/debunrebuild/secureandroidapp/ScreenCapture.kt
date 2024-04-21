package com.debunrebuild.secureandroidapp

import android.app.Activity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

fun preventScreenCapture(activity: AppCompatActivity) {
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}