package com.example.sprint.Models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprint.db.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import android.content.Context
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyViewModel(
    val officerDao: OfficerDao,
    private val tglIdentificationDao: TglIdentificationDao
) : ViewModel() {

    val officers = officerDao.getAllOfficers()
    val tglIdentifications = tglIdentificationDao.getAllTglIdentifications()


    fun insertOfficer(officer: Officer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            officerDao.insertOfficer(officer)
        }
    }

    suspend fun isEmailExists(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            officerDao.isEmailExists(email)
        }
    }

    fun insertOfficerIdentification(tglIdentification: TglIdentification, context: Context, imageUri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // First, save the image to internal storage
                val imageFile = File(context.filesDir, UUID.randomUUID().toString())
                val inputStream = context.contentResolver.openInputStream(imageUri)
                inputStream.use { input ->
                    imageFile.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

                // Then, update the officer identification object with the image path
                tglIdentification.govtIdCardImage = imageFile.absolutePath
                tglIdentificationDao.insertTglIdentification(tglIdentification)
            }
        }
    }
}

