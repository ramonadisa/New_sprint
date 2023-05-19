package com.example.sprint.db

import android.os.Parcelable
import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "officer_data_table")
data class Officer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "officer_id")
    var id: Int,
    @ColumnInfo(name = "officer_name")
    var name: String,
    @ColumnInfo(name = "officer_email")
    var email: String,
    @ColumnInfo(name = "officer_password")
    var password: String
)

@Parcelize
@Entity(tableName = "tgl_identification_table")
data class TglIdentification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tgl_id")
//    var id: Int,
    var id: Int? = null,
    @ColumnInfo(name = "full_name")
    var fullName: String,
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String,
    @ColumnInfo(name = "sex")
    var sex: String,
    @ColumnInfo(name = "date_of_birth")
    var dateOfBirth: String?,
    @ColumnInfo(name = "bvn")
    var bvn: String,
    @ColumnInfo(name = "nin")
    var nin: String,
    @ColumnInfo(name = "state")
    var state: String,
    @ColumnInfo(name = "local_govt_area")
    var localGovtArea: String,
    @ColumnInfo(name = "hub")
    var hub: String,
    @ColumnInfo(name = "govt_id")
    var govtId: String,
    @ColumnInfo(name = "govt_id_type")
    var govtIdType: String,
    @ColumnInfo(name = "govt_id_card_image")
    var govtIdCardImage: String

): Parcelable
