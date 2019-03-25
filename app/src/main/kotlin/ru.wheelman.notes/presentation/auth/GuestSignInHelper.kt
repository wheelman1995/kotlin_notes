package ru.wheelman.notes.presentation.auth

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import ru.wheelman.notes.di.app.AppScope
import javax.inject.Inject

@AppScope
class GuestSignInHelper @Inject constructor(private val context: Context) {

    @RequiresPermission(READ_PHONE_STATE)
    fun getId(): String {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }
}