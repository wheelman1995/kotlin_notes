package ru.wheelman.notes.presentation.noteeditor

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.jetbrains.anko.support.v4.alert
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentNoteEditorBinding
import ru.wheelman.notes.presentation.abstraction.AbstractFragment
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.noteeditor.NoteEditorFragmentViewModel.Factory

class NoteEditorFragment :
    AbstractFragment<NoteEditorFragmentViewModel>() {

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
        initUi()
        initVariables()
        initListeners()
        initBinding()
        return binding.root
    }

    private fun initUi() {
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        super.initListeners()
        viewModel.label.observe(this, Observer {
            mainActivity.supportActionBar?.title = it
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_editor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                true
            }
            R.id.delete_note -> {
                showNoteDeletionDialog()
                true
            }
            R.id.color_picker -> {
                togglePalette()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun showNoteDeletionDialog() {
        alert {
            messageResource = R.string.delete_dialog_message
            negativeButton(R.string.delete_dialog_cancel) { }
            positiveButton(R.string.delete_dialog_ok) { viewModel.deleteNote { navController.navigateUp() } }
        }.show()
    }

    fun togglePalette() {
        if (binding.cpv.isOpen) {
            binding.cpv.close()
        } else {
            binding.cpv.open()
        }
    }

    private fun initVariables() {
        navController = findNavController()
        viewModel = ViewModelProviders.of(this, Factory(requireActivity().application, args.noteId))
            .get(NoteEditorFragmentViewModel::class.java)
        mainActivity = activity as MainActivity
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    override fun onDestroyView() {
        lifecycle.removeObserver(viewModel)
        super.onDestroyView()
    }
}