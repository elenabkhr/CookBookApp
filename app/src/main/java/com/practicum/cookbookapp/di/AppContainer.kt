package com.practicum.cookbookapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.practicum.cookbookapp.data.AppDatabase
import com.practicum.cookbookapp.data.CategoriesDao
import com.practicum.cookbookapp.data.RecipeApiService
import com.practicum.cookbookapp.data.RecipesDao
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.URL_RECIPES
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val categoriesDao: CategoriesDao = db.categoriesDao()
    private val recipesDao: RecipesDao = db.recipesDao()

    private val logging = HttpLoggingInterceptor()
        .apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_RECIPES)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val repository = RecipesRepository(
        context = context.applicationContext,
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val recipeListViewModelFactory = RecipeListViewModelFactory(repository)
}