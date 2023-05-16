package com.example.sprint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sprint.databinding.FragmentTglRegBinding
import com.example.sprint.databinding.FragmentTglTestBinding

class tglTestFragment : Fragment() {
    private lateinit var binding: FragmentTglTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTglTestBinding.inflate(inflater, container, false)
        return binding.root
    }

}