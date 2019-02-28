package ru.wheelman.notes

import android.app.Application
import ru.wheelman.notes.di.components.AppComponent
import ru.wheelman.notes.di.components.DaggerAppComponent

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