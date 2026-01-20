package com.practicum.cookbookapp.data

import android.content.Context
import androidx.core.content.edit
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val context: Context,
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
    private val ioDispatcher: CoroutineDispatcher,
) {

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
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        val stringIds = ids.joinToString(",")
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByIds(stringIds)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategoryById(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                recipeApiService.getCategories()
            } catch (e: Exception) {
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