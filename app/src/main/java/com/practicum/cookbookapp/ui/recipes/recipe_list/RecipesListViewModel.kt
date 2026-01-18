package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.URL_RECIPES
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe
import kotlinx.coroutines.launch

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

    private val recipesRepository = RecipesRepository(application.applicationContext)

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val recipesFromCache = recipesRepository.getRecipesFromCache(categoryId)
            val categoryFromCache = recipesRepository.getCategoryFromCache(categoryId)

            _liveData.value = RecipesListState(
                recipes = recipesFromCache,
                category = categoryFromCache,
                imageUrl = ("$URL_RECIPES/images/${categoryFromCache.imageUrl}")
            )

            val recipesFromBackend = recipesRepository.getRecipesByCategoryId(categoryId)
            val categoryFromBackend = recipesRepository.getCategoryById(categoryId)
            val imageUrlFromBackend = ("$URL_RECIPES/images/${categoryFromBackend?.imageUrl}")

            if (categoryFromBackend != null) {
                if (recipesFromBackend == null) {
                    Log.e("!!!", "Image not found")
                    _errorLiveData.value = "Ошибка получения данных"
                    return@launch
                }

                val recipesWithCategory = recipesFromBackend.map { recipe ->
                    recipe.copy(categoryId = categoryFromBackend.id)
                }

                recipesRepository.recipesDao.insertRecipe(recipesWithCategory)

                _liveData.value =
                    RecipesListState(
                        category = categoryFromBackend,
                        imageUrl = imageUrlFromBackend,
                        recipes = recipesFromBackend
                    )
            }
        }
    }
}