package com.example.sprint.Models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sprint.db.OfficerDao
import com.example.sprint.db.TglIdentificationDao

class MainViewModelFactory(
    private val officerDao: OfficerDao,
    private val tglIdentificationDao: TglIdentificationDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(officerDao, tglIdentificationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



