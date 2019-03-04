package ru.wheelman.notes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.model.repositories.INotesRepository
import javax.inject.Inject

class MainFragmentViewModel : ViewModel(),
    IMainFragmentViewModel {

    init {
        initDagger()

        refreshNotes()
    }

    @Inject
    internal lateinit var notesRepository: INotesRepository
    @Inject
    internal lateinit var notesAdapter: INotesAdapterViewModelMutable
    private val _progress = MutableLiveData<Int>()

    override fun getNotesAdapterViewModel(): INotesAdapterViewModel = notesAdapter

    override fun getProgress() = _progress

    private fun initDagger() {
        val subcomponent = NotesApp.appComponent.mainFragmentViewModelSubcomponent().build()
        subcomponent.inject(this)
    }

    private fun refreshNotes() {
        viewModelScope.launch {
            val channel = Channel<Int>()
            launch {
                for (x in channel) {
                    _progress.value = channel.receive()
                }
            }

            val notes = notesRepository.getNotes(channel)

            notesAdapter.setNewData(notes)
        }
    }
}