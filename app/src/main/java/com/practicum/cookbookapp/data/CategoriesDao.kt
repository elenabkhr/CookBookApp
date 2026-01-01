package com.practicum.cookbookapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.cookbookapp.model.Category

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
    fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)
}