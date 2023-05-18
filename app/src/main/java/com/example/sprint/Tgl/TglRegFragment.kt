package com.example.sprint.Tgl

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
import androidx.lifecycle.ViewModelProvider
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.db.AppDatabase


class TglRegFragment : Fragment() {
    private lateinit var binding: FragmentTglRegBinding
    private lateinit var navController: NavController
    private var datePickerDialog: DatePickerDialog ?= null
    private var selectedFileUri: Uri? = null
    private lateinit var viewModel: MyViewModel
    private var selectedImageUri: Uri? = null
    private var selectedState = ""

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

        val lga = binding.etLGA
        binding.dropDownState.setOnItemClickListener { parent, view, position, id ->
            selectedState = adapterState.getItem(position).toString()
            binding.etLGA.setText("")
            val lgaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, getLGA(selectedState))
            lga.setAdapter(lgaAdapter)
            //selectedState = parent?.selectedItem as String
            Toast.makeText(
                requireContext(),
                "Selected state is: $selectedState",
                Toast.LENGTH_LONG
            ).show()
        }

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
                dropDownSex.text.toString(),
                etDOB.text.toString(),
                etBVN.text.toString(),
                etNIN.text.toString(),
                dropDownState.text.toString(),
                etLGA.text.toString(),
                etHub.text.toString(),
                etID.text.toString(),
                dropDownIDType.text.toString(),
                selectedFileUri.toString(),
            )

            selectedFileUri?.let {
                viewModel.insertTglIdentification(
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
            val sex = dropDownSex.text.toString()
            val dob = etDOB.text.toString()
            val bvn = etBVN.text.toString()
            val nin = etNIN.text.toString()
            val state = dropDownState.text.toString()
            val lga = etLGA.text.toString()
            val hub = etHub.text.toString()
            val id = etID.text.toString()
            val idtype = dropDownIDType.text.toString()
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

    private fun getLGA(state: String): Array<String> {
        return when (state) {
            getString(R.string.Abia) -> resources.getStringArray(R.array.Abia)
            getString(R.string.Adamawa) -> resources.getStringArray(R.array.Adamawa)
            getString(R.string.Akwa) -> resources.getStringArray(R.array.Akwa_Ibom)
            getString(R.string.Anambra) -> resources.getStringArray(R.array.Anambra)
            getString(R.string.Bauchi) -> resources.getStringArray(R.array.Bauchi)
            getString(R.string.Bayelsa) -> resources.getStringArray(R.array.Bayelsa)
            getString(R.string.Benue) -> resources.getStringArray(R.array.Benue)
            getString(R.string.Borno) -> resources.getStringArray(R.array.Borno)
            getString(R.string.Cross_River) -> resources.getStringArray(R.array.Cross_River)
            getString(R.string.Delta) -> resources.getStringArray(R.array.Delta)
            getString(R.string.Ebonyi) -> resources.getStringArray(R.array.Ebonyi)
            getString(R.string.Edo) -> resources.getStringArray(R.array.Edo)
            getString(R.string.Ekiti) -> resources.getStringArray(R.array.Ekiti)
            getString(R.string.Enugu) -> resources.getStringArray(R.array.Enugu)
            getString(R.string.Gombe) -> resources.getStringArray(R.array.Gombe)
            getString(R.string.Imo) -> resources.getStringArray(R.array.Imo)
            getString(R.string.Jigawa) -> resources.getStringArray(R.array.Jigawa)
            getString(R.string.Kaduna) -> resources.getStringArray(R.array.Kaduna)
            getString(R.string.Kano) -> resources.getStringArray(R.array.Kano)
            getString(R.string.Katsina) -> resources.getStringArray(R.array.Katsina)
            getString(R.string.Kebbi) -> resources.getStringArray(R.array.Kebbi)
            getString(R.string.Kogi) -> resources.getStringArray(R.array.Kogi)
            getString(R.string.Kwara) -> resources.getStringArray(R.array.Kwara)
            getString(R.string.Lagos) -> resources.getStringArray(R.array.Lagos)
            getString(R.string.Nasarawa) -> resources.getStringArray(R.array.Nasarawa)
            getString(R.string.Niger) -> resources.getStringArray(R.array.Niger)
            getString(R.string.Ogun) -> resources.getStringArray(R.array.Ogun)
            getString(R.string.Ondo) -> resources.getStringArray(R.array.Ondo)
            getString(R.string.Osun) -> resources.getStringArray(R.array.Osun)
            getString(R.string.Oyo) -> resources.getStringArray(R.array.Oyo)
            getString(R.string.Plateau) -> resources.getStringArray(R.array.Plateau)
            getString(R.string.Rivers) -> resources.getStringArray(R.array.Rivers)
            getString(R.string.Sokoto) -> resources.getStringArray(R.array.Sokoto)
            getString(R.string.Taraba) -> resources.getStringArray(R.array.Taraba)
            getString(R.string.Yobe) -> resources.getStringArray(R.array.Yobe)
            getString(R.string.Zamfara) -> resources.getStringArray(R.array.Zamfara)
            else -> emptyArray()
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


