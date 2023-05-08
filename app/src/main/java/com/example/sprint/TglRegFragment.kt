package com.example.sprint.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sprint.Models.MyViewModel
import com.example.sprint.R
import com.example.sprint.databinding.FragmentTglRegBinding
import com.example.sprint.db.TglIdentification
import android.content.Intent
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.db.AppDatabase
import androidx.room.OnConflictStrategy


class TglRegFragment : Fragment() {
    private lateinit var binding: FragmentTglRegBinding
    private lateinit var navController: NavController
    private var datePickerDialog: DatePickerDialog ?= null
    private var selectedFileUri: Uri? = null
    private lateinit var viewModel: MyViewModel
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTglRegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)

        val dao = AppDatabase.getInstance(requireActivity().application).officerDao()
        val officerdao = AppDatabase.getInstance(requireActivity().application).officerIdentificationDao()

        val factory = MainViewModelFactory(dao,officerdao)
        viewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        val sex = resources.getStringArray(R.array.Sex)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.sex_list, sex)
        binding.dropDownSex.setAdapter(arrayAdapter)

        val state = resources.getStringArray(R.array.State)
        val adapterState = ArrayAdapter(requireContext(), R.layout.state_list, state)
        binding.dropDownState.setAdapter(adapterState)

        val idType = resources.getStringArray(R.array.idType)
        val adapterIDType = ArrayAdapter(requireContext(), R.layout.id_list, idType)
        binding.dropDownIDType.setAdapter(adapterIDType)

        binding.etDOB.setOnClickListener {
            showDatePickerDialog(binding.etDOB)
        }

//        binding.etImage.setOnClickListener { showFileChooser() }

        binding.etImage.setOnClickListener {
            openFileChooser()
        }

        binding.btnSubmit.setOnClickListener {
            checkInputData()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedFileUri = data.data
            binding.etImage.setImageURI(selectedFileUri)
        }
    }


    private fun saveTglData() {
        binding.apply {
            val tglIdentification = TglIdentification(
                0,
                etName.text.toString(),
                etPhoneNumber.text.toString(),
                dropDownSex.text,
                etDOB.text,
                etBVN.text.toString(),
                etNIN.text.toString(),
                dropDownState.text,
                etLGA.text.toString(),
                etHub.text.toString(),
                etID.text.toString(),
                dropDownIDType.text,
                selectedFileUri.toString(),
            )

            selectedFileUri?.let {
                viewModel.insertOfficerIdentification(
                    tglIdentification,
                    requireContext(), // pass the context of the fragment
                    it // pass the selected image Uri
                )
            }
        }
    }


    private fun checkInputData() {
        binding.apply {
            val name = etName.text.toString()
            val number = etPhoneNumber.text.toString()
            val sex = dropDownSex.text
            val dob = etDOB.text.toString()
            val bvn = etBVN.text.toString()
            val nin = etNIN.text.toString()
            val state = dropDownState.text
            val lga = etLGA.text.toString()
            val hub = etHub.text.toString()
            val id = etID.text.toString()
            val idtype = dropDownIDType.text
            val image = selectedFileUri

            when {
                name.length < 5 -> Toast.makeText(activity,
                    "Name must be at least 5 characters long", Toast.LENGTH_LONG).show()
                number.length < 10 -> Toast.makeText(activity,
                    "Phone number must be at least 10 characters long", Toast.LENGTH_LONG).show()
                sex.isEmpty() -> Toast.makeText(activity,
                    "Please select a gender", Toast.LENGTH_LONG).show()
                dob.isEmpty() -> Toast.makeText(activity,
                    "Please enter your date of birth", Toast.LENGTH_LONG).show()
                bvn.length < 11 -> Toast.makeText(activity,
                    "BVN must be at least 11 characters long", Toast.LENGTH_LONG).show()
                nin.length < 11 -> Toast.makeText(activity,
                    "NIN must be at least 11 characters long", Toast.LENGTH_LONG).show()
                state.isEmpty() -> Toast.makeText(activity,
                    "Please select a state", Toast.LENGTH_LONG).show()
                lga.isEmpty() -> Toast.makeText(activity,
                    "Please enter your LGA", Toast.LENGTH_LONG).show()
                hub.isEmpty() -> Toast.makeText(activity,
                    "Please enter your hub", Toast.LENGTH_LONG).show()
                id.isEmpty() -> Toast.makeText(activity,
                    "Please enter your ID number", Toast.LENGTH_LONG).show()
                idtype.isEmpty() -> Toast.makeText(activity,
                    "Please select an ID type", Toast.LENGTH_LONG).show()
                image == null -> Toast.makeText(activity,
                    "Please select an Image", Toast.LENGTH_LONG).show()
                else -> {
                    // All fields are valid
                    saveTglData()
                    Toast.makeText(activity, "Registration Successful!",
                        Toast.LENGTH_LONG).show()
                    clearInput()
                }
            }
        }
    }

    private fun clearInput(){
        binding.apply {
            etName.setText("")
            etPhoneNumber.setText("")
            dropDownSex.setText("")
            etDOB.setText("")
            etBVN.setText("")
            etNIN.setText("")
            dropDownState.setText("")
            etLGA.setText("")
            etHub.setText("")
            etID.setText("")
            dropDownIDType.setText("")
            etImage.setImageURI(null)
        }
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                val strMonth = if (month < 10) "0$month" else "$month"
                val strDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val date = "$strDayOfMonth-$strMonth-$year"
                textView.setText(date)
            }, year, month, dayOfMonth
        )
        datePickerDialog!!.setTitle("Select Date")
        datePickerDialog!!.show()
    }

}


