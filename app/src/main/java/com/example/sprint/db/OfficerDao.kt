package com.example.sprint.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OfficerDao {
    @Insert
    suspend fun insertOfficer(officer: Officer)

    @Update
    suspend fun updateOfficer(officer: Officer)

    @Delete
    suspend fun deleteOfficer(officer: Officer)

    @Query("SELECT EXISTS(SELECT 1 FROM officer_data_table WHERE officer_email = :email)")
    suspend fun isEmailExists(email: String): Boolean


    @Query("SELECT * FROM officer_data_table")
    fun getAllOfficers(): LiveData<List<Officer>>
}

@Dao
interface TglIdentificationDao {
    @Insert
    suspend fun insertTglIdentification(tglIdentification: TglIdentification)


    @Update
    suspend fun updateTglIdentification(tglIdentification: TglIdentification)

    @Delete
    suspend fun deleteTglIdentification(tglIdentification: TglIdentification)

    @Query("SELECT * FROM tgl_identification_table")
    fun getAllTglIdentifications(): LiveData<List<TglIdentification>>


}



