package ru.wheelman.notes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.wheelman.notes.NotesApp
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentMainBinding
import ru.wheelman.notes.viewmodel.IMainFragmentViewModel
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    internal lateinit var viewModel: IMainFragmentViewModel
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        initDagger()

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.fragment = this

        navController = findNavController()

        return binding.root
    }

    fun onClick(v: View) {
        navController.navigate(R.id.action_mainFragment_to_secondFragment)
    }

    private fun initDagger() {
        val mainFragmentSubcomponent =
            NotesApp.appComponent.mainFragmentSubcomponent()
                .mainFragment(this)
                .build()
        mainFragmentSubcomponent.inject(this)
    }
}