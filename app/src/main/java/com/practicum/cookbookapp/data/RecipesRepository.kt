package com.practicum.cookbookapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class RecipesRepository {
    private val contentType = "application/json".toMediaType()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("$URL_RECIPES/")
        .addConverterFactory(Json.Default.asConverterFactory(contentType))
        .build()

    val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipeById(recipeId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        val stringIds = ids.joinToString(",")
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipesByIds(stringIds)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoryById(id: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                service.getCategoryById(id)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                service.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                service.getCategories()
            } catch (e: Exception) {
                null
            }
        }
    }
}