package com.practicum.cookbookapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
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

    fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            service.getRecipeById(recipeId).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        val stringIds = ids.joinToString(",")
        return try {
            service.getRecipesByIds(stringIds).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            service.getRecipesByCategoryId(categoryId).execute().body()
        } catch (e: Exception) {
            null
        }
    }

    fun getCategories(): List<Category>? {
        return try {
            service.getCategories().execute().body()
        } catch (e: Exception) {
            null
        }
    }
}