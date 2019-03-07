package ru.wheelman.notes.presentation.noteeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.wheelman.notes.databinding.FragmentNoteEditorBinding
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragmentViewModel.Factory

class NoteEditorFragment : Fragment() {

    private lateinit var binding: FragmentNoteEditorBinding
    private lateinit var viewModel: NoteEditorFragmentViewModel
    private val args: NoteEditorFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        initVariables()
        initListeners()
        initBinding()
        return binding.root
    }

    private fun initListeners() {}

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> navController.navigateUp()
            else -> super.onOptionsItemSelected(item)
        }

    private fun initVariables() {
        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this, Factory(args.noteId))
            .get(NoteEditorFragmentViewModel::class.java)
        navController = findNavController()
    }
}