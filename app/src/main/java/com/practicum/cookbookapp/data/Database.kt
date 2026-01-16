package com.practicum.cookbookapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}