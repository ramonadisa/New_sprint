//package com.example.sprint
//
//import android.text.Editable
//import android.text.SpannableStringBuilder
//import androidx.room.TypeConverter
//import java.util.*
//
//class EditableConverter {
//    @TypeConverter
//    fun fromEditable(editable: Editable?): String? {
//        return editable?.toString()
//    }
//    @TypeConverter
//    fun toEditable(value: String?): Editable? {
//        return value?.let { SpannableStringBuilder(it) }
//    }
//}
//
//class DateConverter {
//    @TypeConverter
//    fun toDate(timestamp: Long): Date {
//        return Date(timestamp)
//    }
//
//    @TypeConverter
//    fun toTimestamp(date: Date): Long {
//        return date.time
//    }
//}
