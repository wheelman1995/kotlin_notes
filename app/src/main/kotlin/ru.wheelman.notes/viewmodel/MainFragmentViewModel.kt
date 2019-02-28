package ru.wheelman.notes.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class MainFragmentViewModel : ViewModel(), IMainFragmentViewModel {

    override fun okButtonPressed() {
        Log.d(this.toString(), "Button was pressed!")
    }
}