package ru.wheelman.notes.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.wheelman.notes.R
import ru.wheelman.notes.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentSecondBinding.inflate(inflater, container, false)
        binding.fragment = this

        navController = findNavController()

        return binding.root
    }

    fun onClick(v: View) {
        navController.navigate(R.id.action_secondFragment_to_mainFragment)
    }
}