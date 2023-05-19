package com.example.sprint.Tgl

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.sprint.Models.MainViewModelFactory
import com.example.sprint.Models.MyViewModel
import com.example.sprint.R
import com.example.sprint.databinding.FragmentTglUpdateBinding
import com.example.sprint.db.AppDatabase
import com.example.sprint.db.TglIdentification

class TglUpdateFragment : Fragment() {
    private lateinit var navController: NavController
    private var datePickerDialog: DatePickerDialog?= null
    private var selectedFileUri: Uri? = null
    private lateinit var viewModel: MyViewModel
    private var selectedImageUri: Uri? = null
    private var selectedState = ""
    private val args by navArgs<TglUpdateFragmentArgs>()
    private lateinit var binding: FragmentTglUpdateBinding

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

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
        binding.dropDownSex1.setAdapter(arrayAdapter)

        val state = resources.getStringArray(R.array.State)
        val adapterState = ArrayAdapter(requireContext(), R.layout.state_list, state)
        binding.dropDownState1.setAdapter(adapterState)

        val idType = resources.getStringArray(R.array.idType)
        val adapterIDType = ArrayAdapter(requireContext(), R.layout.id_list, idType)
        binding.dropDownIDType1.setAdapter(adapterIDType)

        binding.etDOB1.setOnClickListener {
            showDatePickerDialog(binding.etDOB1)
        }

        val lga = binding.etLGA1
        binding.dropDownState1.setOnItemClickListener { parent, view, position, id ->
            selectedState = adapterState.getItem(position).toString()
            binding.etLGA1.setText("")
            val lgaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, getLGA(selectedState))
            lga.setAdapter(lgaAdapter)
        }

        binding.etImage1.setOnClickListener {
            openFileChooser()
        }

        binding.btnUpdateTgl.setOnClickListener {
            checkInputData()
            navController.navigate(R.id.action_tglUpdateFragment2_to_tglTestFragment)
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, TglUpdateFragment.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TglUpdateFragment.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedFileUri = data.data
            binding.etImage1.setImageURI(selectedFileUri)
        }
    }

    private fun checkInputData() {
        binding.apply {
            val name = etName1.text.toString()
            val number = etPhoneNumber1.text.toString()
            val sex = dropDownSex1.text.toString()
            val dob = etDOB1.text.toString()
            val bvn = etBVN1.text.toString()
            val nin = etNIN1.text.toString()
            val state = dropDownState1.text.toString()
            val lga = etLGA1.text.toString()
            val hub = etHub1.text.toString()
            val id = etID1.text.toString()
            val idtype = dropDownIDType1.text.toString()
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
                    updateTglData()
                    Toast.makeText(activity, "Update Successful!",
                        Toast.LENGTH_LONG).show()
                    clearInput()
                }
            }
        }
    }

    private fun updateTglData() {
        binding.apply {
            val tglIdentification = TglIdentification(
                null,
                etName1.text.toString(),
                etPhoneNumber1.text.toString(),
                dropDownSex1.text.toString(),
                etDOB1.text.toString(),
                etBVN1.text.toString(),
                etNIN1.text.toString(),
                dropDownState1.text.toString(),
                etLGA1.text.toString(),
                etHub1.text.toString(),
                etID1.text.toString(),
                dropDownIDType1.text.toString(),
                selectedFileUri.toString(),
            )

            selectedFileUri?.let {
                viewModel.updateTglIdentification(
                    tglIdentification,
                    requireContext(), // pass the context of the fragment
                    it // pass the selected image Uri
                )
            }
        }
    }

    private fun clearInput(){
        binding.apply {
            etName1.setText("")
            etPhoneNumber1.setText("")
            dropDownSex1.setText("")
            etDOB1.setText("")
            etBVN1.setText("")
            etNIN1.setText("")
            dropDownState1.setText("")
            etLGA1.setText("")
            etHub1.setText("")
            etID1.setText("")
            dropDownIDType1.setText("")
            etImage1.setImageURI(null)
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
















