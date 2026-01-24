package com.practicum.cookbookapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.practicum.cookbookapp.data.AppDatabase
import com.practicum.cookbookapp.data.CategoriesDao
import com.practicum.cookbookapp.data.RecipeApiService
import com.practicum.cookbookapp.data.RecipesDao
import com.practicum.cookbookapp.data.URL_RECIPES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule() {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesCategoriesDao(appDatabase: AppDatabase): CategoriesDao = appDatabase.categoriesDao()

    @Provides
    fun providesRecipesDao(appDatabase: AppDatabase): RecipesDao = appDatabase.recipesDao()

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(URL_RECIPES)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}