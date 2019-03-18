package ru.wheelman.notes.presentation.utils

import android.content.Context
import android.preference.PreferenceManager
import ru.wheelman.notes.di.app.AppScope
import ru.wheelman.notes.model.entities.AuthMode
import javax.inject.Inject

@AppScope
class PreferenceHelper @Inject constructor(private val context: Context) {

    private companion object {
        private const val AUTH_MODE_KEY = "auth_mode"
    }

    private val sp = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sp.edit()

    fun putAuthMode(mode: AuthMode) {
        editor.putString(AUTH_MODE_KEY, mode.name).apply()
    }

    fun getAuthMode(): AuthMode {
        val mode = sp.getString(AUTH_MODE_KEY, AuthMode.UNAUTHORIZED.name)
        return AuthMode.valueOf(mode)
    }
}