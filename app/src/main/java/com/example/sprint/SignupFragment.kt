package com.example.sprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.sprint.databinding.FragmentSignupBinding
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.Models.MyViewModel
import com.example.sprint.db.AppDatabase
import com.example.sprint.db.Officer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.lifecycle.lifecycleScope


class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)

        val dao = AppDatabase.getInstance(requireActivity().application).officerDao()
        val officerdao = AppDatabase.getInstance(requireActivity().application).officerIdentificationDao()

        val factory = MainViewModelFactory(dao,officerdao)
        viewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        binding.btnSignup.setOnClickListener {
            checkDetails()
        }
    }

    private fun saveOfficerData(){
        binding.apply {
            viewModel.insertOfficer(
                Officer(
                    0,
                    etSUName.text.toString(),
                    etSUEmail.text.toString(),
                    etSUPassword.text.toString()
                )
            )
        }

    }


    private fun checkDetails(){
        binding.apply {
            val name = etSUName.text.toString()
            val mail = etSUEmail.text.toString()
            val password = etSUPassword.text.toString()
            val confirmPassword = etSUConfirmPassword.text.toString()

            if (name.length < 5) {
                Toast.makeText(requireContext(), "Name should be at least 5 characters long",
                    Toast.LENGTH_SHORT).show()
                return
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(requireContext(), "Invalid email format",
                    Toast.LENGTH_SHORT).show()
                return
            }

            if (password.length < 5) {
                Toast.makeText(requireContext(), "Password should be at least 5 characters long",
                    Toast.LENGTH_SHORT).show()
                return
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match",
                    Toast.LENGTH_SHORT).show()
                return
            }

                lifecycleScope.launch {
                    val isEmailExists = viewModel.isEmailExists(mail)
                    if (isEmailExists) {
                        Toast.makeText(
                            requireContext(), "Email already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        saveOfficerData()
                        Toast.makeText(
                            requireContext(), "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate(R.id.action_signupFragment_to_loginFragment)
                        clearInput()
                    }
                }
        }
    }

    private fun clearInput(){
        binding.apply {
            etSUName.setText("")
            etSUEmail.setText("")
            etSUPassword.setText("")
            etSUConfirmPassword.setText("")
        }
    }
}
