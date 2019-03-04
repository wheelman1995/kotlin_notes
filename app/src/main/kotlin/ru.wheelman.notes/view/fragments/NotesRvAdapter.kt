package ru.wheelman.notes.view.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import ru.wheelman.notes.databinding.ItemNoteBinding
import ru.wheelman.notes.logd
import ru.wheelman.notes.view.fragments.NotesRvAdapter.ViewHolder
import ru.wheelman.notes.viewmodel.INotesAdapterViewModel

class NotesRvAdapter(
    private val notesAdapterViewModel: INotesAdapterViewModel,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ViewHolder>(), LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        notesAdapterViewModel.subscribe { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.adapter = notesAdapterViewModel

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = notesAdapterViewModel.getItemCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.position = position
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun unsubscribe() {
        logd("Lifecycle.Event.ON_DESTROY")
        notesAdapterViewModel.unsubscribe()
    }

    inner class ViewHolder(internal val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)
}