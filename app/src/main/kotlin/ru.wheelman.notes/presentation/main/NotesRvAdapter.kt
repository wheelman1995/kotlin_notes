package ru.wheelman.notes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import ru.wheelman.notes.databinding.ItemNoteBinding
import ru.wheelman.notes.presentation.main.MainFragmentViewModel.NotesAdapterViewModel
import ru.wheelman.notes.presentation.main.NotesRvAdapter.ViewHolder

class NotesRvAdapter(
    private val notesAdapterViewModel: NotesAdapterViewModel,
    val onCardClickListener: ((String) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>(), LifecycleObserver {

    init {
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

    internal fun onDestroyView() {
        notesAdapterViewModel.unsubscribe()
    }

    inner class ViewHolder(internal val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)
}