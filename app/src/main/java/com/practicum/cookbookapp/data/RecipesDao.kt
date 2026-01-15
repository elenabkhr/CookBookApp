package com.practicum.cookbookapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.cookbookapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes")
    suspend fun getAll(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)
}