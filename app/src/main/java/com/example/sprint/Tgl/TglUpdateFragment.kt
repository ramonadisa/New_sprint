package com.example.sprint.Tgl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.sprint.R
import com.example.sprint.databinding.FragmentTglUpdateBinding

class TglUpdateFragment : Fragment() {
    private val args by navArgs<TglUpdateFragmentArgs>()
    private lateinit var binding: FragmentTglUpdateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTglUpdateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etName1.setText(args.updateTgls.fullName)
            etPhoneNumber1.setText(args.updateTgls.phoneNumber)
            dropDownSex1.setText(args.updateTgls.sex)
            etDOB1.setText(args.updateTgls.dateOfBirth)
            etBVN1.setText(args.updateTgls.bvn)
            etNIN1.setText(args.updateTgls.nin)
            dropDownState1.setText(args.updateTgls.state)
            etLGA1.setText(args.updateTgls.localGovtArea)
            etHub1.setText(args.updateTgls.hub)
            dropDownIDType1.setText(args.updateTgls.govtIdType)
            etID1.setText(args.updateTgls.govtId)
            // etImage1.setImageURI(args.updateTgls.govtIdCardImage)
        }

    }

}