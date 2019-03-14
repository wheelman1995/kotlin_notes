package ru.wheelman.notes.presentation.main

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentMainBinding
import ru.wheelman.notes.presentation.abstraction.AbstractFragment
import ru.wheelman.notes.presentation.activity.MainActivity
import ru.wheelman.notes.presentation.app.NotesApp
import ru.wheelman.notes.presentation.utils.DisplayMetricsHelper
import javax.inject.Inject

class MainFragment : AbstractFragment<MainFragmentViewModel>() {

    @Inject
    internal lateinit var displayMetricsHelper: DisplayMetricsHelper
    private lateinit var navController: NavController
    private lateinit var notesAdapter: NotesRvAdapter
    private lateinit var mainActivity: MainActivity
    private lateinit var fab: FloatingActionButton
    private val fabMargins = 16
    private lateinit var binding: FragmentMainBinding
    private lateinit var fabLayoutParams: LayoutParams
    override lateinit var viewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        initDagger()
        initVariables()
        createFab()
        initListeners()
        initBinding()
        return binding.root
    }

    private fun initVariables() {
        mainActivity = activity as MainActivity
        viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        navController = findNavController()
        notesAdapter = NotesRvAdapter(viewModel.notesAdapter) {
            navController.navigate(MainFragmentDirections.actionMainFragmentToNoteEditorFragment(it))
        }
    }

    override fun initListeners() {
        super.initListeners()
        fab.setOnClickListener {
            navController.navigate(
                MainFragmentDirections.actionMainFragmentToNoteEditorFragment(null)
            )
        }
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.adapter = notesAdapter
    }

    private fun initDagger() {
        NotesApp.appComponent.inject(this)
    }

    private fun createFab() {
        fab = FloatingActionButton(mainActivity).apply {
            setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.ic_add_black_24dp))
        }
        fabLayoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END or Gravity.BOTTOM
            setMargins(displayMetricsHelper.dpToPx(fabMargins))
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.addView(fab, fabLayoutParams)
    }

    override fun onStop() {
        mainActivity.removeView(fab)
        super.onStop()
    }

    override fun onDestroyView() {
        notesAdapter.onDestroyView()
        super.onDestroyView()
    }
}