package ru.wheelman.notes

import android.app.Application
import android.util.Log
import ru.wheelman.notes.di.components.AppComponent
import ru.wheelman.notes.di.components.DaggerAppComponent

fun <T : Any> T.logd(msg: Any?) {
    Log.d(this::class.simpleName, " $msg")
}

class NotesApp : Application() {

    internal companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .notesApp(this)
            .build()
    }
}