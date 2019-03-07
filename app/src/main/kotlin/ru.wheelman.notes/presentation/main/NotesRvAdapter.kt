package ru.wheelman.notes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import ru.wheelman.notes.databinding.ItemNoteBinding
import ru.wheelman.notes.presentation.main.MainFragmentViewModel.NotesAdapterViewModel
import ru.wheelman.notes.presentation.main.NotesRvAdapter.ViewHolder

class NotesRvAdapter(
    private val notesAdapterViewModel: NotesAdapterViewModel,
    private val lifecycle: Lifecycle,
    val onCardClickListener: ((String) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>(), LifecycleObserver {

    init {
        lifecycle.addObserver(this)
        notesAdapterViewModel.subscribe { notifyDataSetChanged() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.adapterViewModel = notesAdapterViewModel
        binding.adapter = this
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = notesAdapterViewModel.getItemCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.position = position
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun unsubscribe() {
        lifecycle.removeObserver(this)
        notesAdapterViewModel.unsubscribe()
    }

    inner class ViewHolder(internal val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)
}