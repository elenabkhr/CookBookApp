package com.practicum.cookbookapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.URL_RECIPES
import com.practicum.cookbookapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isLoading: Boolean = false,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val imageUrl: String? = null,
    )

    private val _liveData = MutableLiveData<RecipeState>()
    val liveData: LiveData<RecipeState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun loadRecipe(id: Int) {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipeById(id)
            val imageUrl = ("${URL_RECIPES}images/${recipe?.imageUrl}")

            if (recipe == null) {
                Log.e("!!!", "Image not found")
                _errorLiveData.value = "Ошибка получения данных"
                return@launch
            }

            _liveData.value = RecipeState(
                recipe = recipe,
                isFavorite = recipesRepository.getFavorites().contains(id.toString()),
                portionsCount = _liveData.value?.portionsCount ?: 1,
                imageUrl = imageUrl
            )
        }
    }

    fun onFavoritesClicked() {
        val recipeId = _liveData.value?.recipe?.id
        val setFavorites = recipesRepository.getFavorites()
        val isFavorite = setFavorites.contains(recipeId.toString())

        if (isFavorite) {
            setFavorites.remove(recipeId.toString())
        } else {
            setFavorites.add(recipeId.toString())
        }
        recipesRepository.saveFavorites(setFavorites)

        _liveData.value = _liveData.value?.copy(isFavorite = !isFavorite)
    }

    fun updatePortionsCount(portions: Int) {
        _liveData.value = _liveData.value?.copy(portionsCount = portions)
    }
}