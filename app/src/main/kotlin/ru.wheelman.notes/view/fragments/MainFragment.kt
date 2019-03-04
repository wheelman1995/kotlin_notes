package ru.wheelman.notes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import ru.wheelman.notes.databinding.FragmentMainBinding
import ru.wheelman.notes.logd
import ru.wheelman.notes.view.MainActivity
import ru.wheelman.notes.viewmodel.IMainFragmentViewModel
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    internal lateinit var viewModel: IMainFragmentViewModel
    @Inject
    internal lateinit var navController: NavController
    @Inject
    internal lateinit var notesAdapter: NotesRvAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initDagger()

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.adapter = notesAdapter

        return binding.root
    }

    private fun initDagger() {
        val mainFragmentSubcomponent =
            (activity as MainActivity).mainActivitySubcomponent
                .mainFragmentSubcomponent()
                .mainFragment(this)
                .build()
        mainFragmentSubcomponent.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logd("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        logd("onDetach")
        super.onDetach()
    }

    override fun onDestroyView() {
        logd("onDestroyView")
        super.onDestroyView()
    }

    override fun onStop() {
        logd("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logd("onDestroy")
        super.onDestroy()
    }
}