package com.example.sprint.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.Models.MyViewModel
import com.example.sprint.R
import com.example.sprint.databinding.FragmentLoginBinding
import com.example.sprint.db.AppDatabase

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val dao = AppDatabase.getInstance(requireActivity().application).officerDao()
                val officerdao = AppDatabase.getInstance(requireActivity().application).officerIdentificationDao()
                val factory = MainViewModelFactory(dao, officerdao)
                val viewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

                viewModel.officers.observe(viewLifecycleOwner, Observer { officers ->
                    val officer = officers.find { it.email == email && it.password == password }
                    if (officer != null) {
                        navController.navigate(R.id.action_loginFragment_to_functionalitiesFragment)
                        clearInput()
                    } else {
                        Toast.makeText(activity,"Invalid email or password",Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(activity,"input fields cannot be empty",Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignup.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    private fun clearInput(){
        binding.apply {
            etEmail.setText("")
            etPassword.setText("")
        }
    }
}
