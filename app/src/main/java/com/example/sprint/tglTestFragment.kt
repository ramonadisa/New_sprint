package com.example.sprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.Models.MyViewModel
import com.example.sprint.databinding.FragmentTglRegBinding
import com.example.sprint.databinding.FragmentTglTestBinding
import com.example.sprint.db.AppDatabase
import com.example.sprint.db.TglIdentification

class tglTestFragment : Fragment() {
    private lateinit var binding: FragmentTglTestBinding
    private lateinit var navController: NavController
    private lateinit var  viewModel: MyViewModel
    private lateinit var adapter: TglRecyclerViewAdapter
    private var isListItemClicked = false
    private lateinit var selectedTgl:TglIdentification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTglTestBinding.inflate(inflater, container, false)
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

        initRecyclerView()

    }

    private fun initRecyclerView(){
        binding.rvTgl.layoutManager = LinearLayoutManager(requireContext())
        adapter = TglRecyclerViewAdapter { selectedItem: TglIdentification ->
            listItemClicked(selectedItem)
        }
        binding.rvTgl.adapter = adapter

        displayStudentsList()
    }

    private fun displayStudentsList(){
        viewModel.tglIdentifications.observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listItemClicked(tglIdentification:TglIdentification){
        binding.apply {
            selectedTgl = tglIdentification
//            btnSave.text = "Update"
//            btnClear.text = "Delete"
            isListItemClicked = true
//            etName.setText(selectedTgl.fullName)
//            etEmail.setText(selectedTgl.phoneNumber)
        }
    }
}


















