package com.example.dwello.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dwello.utils.Converters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dwello.data.PropertyDao
import com.example.dwello.data.PropertyEntity

@Database(entities = [PropertyEntity::class], version = 2, exportSchema = false) // Increment the version number
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Drop the old table if it exists
        database.execSQL("DROP TABLE IF EXISTS properties")

        // Create the new table with the updated column names
        database.execSQL("""
            CREATE TABLE properties (
                pid TEXT PRIMARY KEY NOT NULL,
                about_home TEXT NOT NULL,
                baths REAL NOT NULL,
                beds INTEGER NOT NULL,
                built_year INTEGER NOT NULL,
                city TEXT NOT NULL,
                est_monthly INTEGER NOT NULL,
                hoa INTEGER NOT NULL,
                image_urls TEXT NOT NULL,
                list_date INTEGER NOT NULL,
                parking_space INTEGER NOT NULL,
                price INTEGER NOT NULL,
                property_type TEXT NOT NULL,
                state TEXT NOT NULL,
                street TEXT NOT NULL,
                sqft INTEGER NOT NULL,
                thumbnail_url TEXT NOT NULL,
                zipcode TEXT NOT NULL,
                lat REAL NOT NULL,
                lng REAL NOT NULL
            )
        """)
    }
}