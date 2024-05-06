package com.debunrebuild.secureandroidapp

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

fun checkBiometricAuthenticationAvailability(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
    bioMetricAuthOptions: BioMetricAuthOptions
) {
    val biometricManager = BiometricManager.from(activity)
    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            biometric(activity, bioMetricAuthCallBack, bioMetricAuthOptions)
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Log.e("MY_APP_TAG", "No biometric features available on this device.")
            bioMetricAuthCallBack.onError()
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            bioMetricAuthCallBack.onError()
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // Prompts the user to create credentials that your app accepts.
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }
            val authAvailabilityResult = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

                })
            authAvailabilityResult.launch(enrollIntent)
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            // Do nothing
            bioMetricAuthCallBack.onError()
        }

        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            // Do nothing
            bioMetricAuthCallBack.onError()
        }

        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
            // Do nothing
            bioMetricAuthCallBack.onError()
        }
    }
}


private fun biometric(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
    bioMetricAuthOptions: BioMetricAuthOptions
) {

    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    activity.applicationContext,
                    "Authentication error: $errString", Toast.LENGTH_SHORT
                )
                    .show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                bioMetricAuthCallBack.onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(bioMetricAuthOptions.getTitle())
        .setSubtitle(bioMetricAuthOptions.getMessage())
    if (bioMetricAuthOptions.isOnlyBiometricAuth()) {
        promptInfo.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText(bioMetricAuthOptions.getNegativeMessage())
    } else {
        promptInfo.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
    }
    biometricPrompt.authenticate(promptInfo.build())

    // Prompt appears when user clicks "Log in".
    // Consider integrating with the keystore to unlock cryptographic operations,
    // if needed by your app.

}

interface BioMetricAuthCallBack {
    fun onSuccess()
    fun onError()
}

class BioMetricAuthOptions() {
    private var forceBiometric = false
    private var title: String = "Authenticate to access"
    private var message: String = "Please Authenticate using Biometric prompt to access"
    private var negativeMessage: String = ""
    private var enrollIfNotEnrolled = true

    fun getTitle() = title
    fun getMessage() = message

    fun getNegativeMessage() = negativeMessage

    fun isOnlyBiometricAuth() = forceBiometric

    class Builder {
        private var forceBiometric = false
        private var title: String = "Authenticate to access"
        private var message: String = "Please Authenticate using Biometric prompt to access"
        private var negativeMessage: String = ""
        private var enrollIfNotEnrolled = true
        fun forceBiometricOnly(negativeMessage: String): Builder {
            forceBiometric = true
            this.negativeMessage = negativeMessage
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun shouldTryToEnrollOnError(shouldEnroll: Boolean) {
            enrollIfNotEnrolled = shouldEnroll
        }


        fun build(): BioMetricAuthOptions {
            val bioMetricAuthOptions = BioMetricAuthOptions()
            bioMetricAuthOptions.forceBiometric = this.forceBiometric
            bioMetricAuthOptions.message = message
            bioMetricAuthOptions.title = title
            bioMetricAuthOptions.negativeMessage = negativeMessage
            bioMetricAuthOptions.enrollIfNotEnrolled = enrollIfNotEnrolled
            return bioMetricAuthOptions
        }

    }
}

