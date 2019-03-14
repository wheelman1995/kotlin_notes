package ru.wheelman.notes.presentation.app

import android.app.Application
import android.util.Log
import ru.wheelman.notes.di.app.AppComponent
import ru.wheelman.notes.di.app.DaggerAppComponent

fun <T : Any> T.logd(msg: Any?) = Log.d(this::class.simpleName, " $msg")

class NotesApp : Application() {

    internal companion object {
        internal lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .app(this)
            .build()
    }

}