package com.practicum.cookbookapp.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RecipesRepository @Inject constructor(
    private val context: Context,
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
) {

    private val ioDispatcher: CoroutineContext = Dispatchers.IO

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(ioDispatcher) {
            categoriesDao.getAll()
        }
    }

    suspend fun getCategoryFromCache(categoryId: Int): Category {
        return withContext(ioDispatcher) {
            categoriesDao.getCategoryById(categoryId)
        }
    }

    suspend fun insertCategoriesIntoCache(categories: List<Category>) {
        return withContext(ioDispatcher) {
            categoriesDao.insertCategoriesIntoCache(categories)
        }
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesByCategoryId(categoryId)
        }
    }

    suspend fun getFavoritesFromCache(): List<Recipe>? {
        return withContext(ioDispatcher) {
            recipesDao.getRecipesFavorite()
        }
    }

    suspend fun insertRecipesIntoCache(recipes: List<Recipe>) {
        return withContext(ioDispatcher) {
            recipesDao.insertRecipesIntoCache(recipes)
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipeById(recipeId)
            } catch (e: Exception) {
                Log.e("RecipesRepository", "Failed to get recipe by id", e)
                null
            }
        }
    }

    suspend fun getRecipesByIds(recipesId: Set<Int>): List<Recipe>? {
        val stringIds = recipesId.joinToString(",")
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByIds(stringIds)
            } catch (e: Exception) {
                Log.e("RecipesRepository", "Failed to get recipes by ids", e)
                null
            }
        }
    }

    suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategoryById(categoryId)
            } catch (e: Exception) {
                Log.e("RecipesRepository", "Failed to get category by id", e)
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                Log.e("RecipesRepository", "Failed to get recipes by category id", e)
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategories()
            } catch (e: Exception) {
                Log.e("RecipesRepository", "Failed to get categories", e)
                null
            }
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }

    fun saveFavorites(setStringId: Set<String>) {
        val sharedPrefs = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit {
            putStringSet(FAVORITES_KEY, setStringId)
        }
    }
}