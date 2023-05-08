package com.example.sprint.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sprint.R
import com.example.sprint.databinding.FragmentFunctionalitiesBinding
import com.example.sprint.databinding.FragmentLoginBinding

class FunctionalitiesFragment : Fragment() {
    private lateinit var binding: FragmentFunctionalitiesBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFunctionalitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)

        binding.btnRecReg.setOnClickListener {
            navController.navigate(R.id.action_functionalitiesFragment_to_tglRegFragment)
        }
    }
}