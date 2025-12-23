package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.AppExecutors
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.URL_RECIPES
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val recipes: List<Recipe>? = emptyList(),
        val category: Category? = null,
        val imageUrl: String? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<RecipesListState>()
    val liveData: LiveData<RecipesListState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository()

    fun loadRecipesList(categoryId: Int) {
        AppExecutors.threadPool.execute {
            val recipes = recipesRepository.getRecipesByCategoryId(categoryId)
            val category = recipesRepository.getCategoryById(categoryId)

            val imageUrl = ("$URL_RECIPES/images/${category?.imageUrl}")

            if (recipes == null) {
                Log.e("!!!", "Image not found")
                _errorLiveData.postValue("Ошибка получения данных")
                return@execute
            }

            _liveData.postValue(
                RecipesListState(category = category, imageUrl = imageUrl, recipes = recipes)
            )
        }
    }
}