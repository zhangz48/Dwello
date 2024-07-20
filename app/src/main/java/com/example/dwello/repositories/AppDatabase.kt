package com.example.dwello.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dwello.data.PropertyDao
import com.example.dwello.data.PropertyEntity
import com.example.dwello.utils.Converters

@Database(entities = [PropertyEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
}