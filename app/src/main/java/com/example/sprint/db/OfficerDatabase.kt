package com.example.sprint.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Officer::class, TglIdentification::class], version = 15, exportSchema = false)
//@TypeConverters(EditableConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun officerDao(): OfficerDao
    abstract fun officerIdentificationDao(): TglIdentificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                    INSTANCE= instance
                instance
            }
        }

    }
}