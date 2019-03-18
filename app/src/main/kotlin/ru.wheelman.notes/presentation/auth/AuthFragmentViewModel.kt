package ru.wheelman.notes.presentation.auth

import androidx.lifecycle.ViewModel
import ru.wheelman.notes.presentation.app.NotesApp

class AuthFragmentViewModel : ViewModel() {

    init {
        initDagger()
    }

//    private val _authenticated = MutableLiveData<Boolean>()
//    internal val authenticated: LiveData<Boolean> = _authenticated

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }
}