package com.example.dwello.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties")
    suspend fun getAllProperties(): List<PropertyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(properties: List<PropertyEntity>)

    @Query("DELETE FROM properties")
    suspend fun deleteAll()
}