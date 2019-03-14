package ru.wheelman.notes.presentation.noteeditor

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.wheelman.notes.databinding.FragmentNoteEditorBinding
import ru.wheelman.notes.presentation.abstraction.AbstractFragment
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragmentViewModel.Factory
import javax.inject.Inject

class NoteEditorFragment :
    AbstractFragment<NoteEditorFragmentViewModel>() {

    @Inject
    internal lateinit var app: Application
    private lateinit var binding: FragmentNoteEditorBinding
    private val args: NoteEditorFragmentArgs by navArgs()
    private lateinit var navController: NavController
    override lateinit var viewModel: NoteEditorFragmentViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteEditorBinding.inflate(inflater, container, false)
        initDagger()
        initVariables()
        initListeners()
        initBinding()
        return binding.root
    }

    override fun initListeners() {
        super.initListeners()
        viewModel.label.observe(this, Observer {
            mainActivity.supportActionBar?.title = it
        })
    }

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
        viewModel = ViewModelProviders.of(this, Factory(app, args.noteId))
            .get(NoteEditorFragmentViewModel::class.java)
        navController = findNavController()
        mainActivity = activity as MainActivity
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }
}