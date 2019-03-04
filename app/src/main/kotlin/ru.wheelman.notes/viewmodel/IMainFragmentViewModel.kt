package ru.wheelman.notes.viewmodel

import androidx.lifecycle.LiveData

interface IMainFragmentViewModel {

    fun getNotesAdapterViewModel(): INotesAdapterViewModel
    fun getProgress(): LiveData<Int>

}
