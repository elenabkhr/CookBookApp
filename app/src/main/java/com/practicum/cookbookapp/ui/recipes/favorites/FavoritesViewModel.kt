package com.practicum.cookbookapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    data class FavoritesState(
        val recipes: List<Recipe>? = emptyList(),
        val favorites: Set<Int> = emptySet(),
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<FavoritesState>()
    val liveData: LiveData<FavoritesState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun loadFavorites() {
        viewModelScope.launch {
            val getFavoritesId = recipesRepository.getFavorites().map { it.toInt() }.toSet()

            if (getFavoritesId.isEmpty()) {
                _liveData.value = FavoritesState(recipes = emptyList(), favorites = emptySet())
                return@launch
            }

            val recipesFromCache = recipesRepository.getFavoritesFromCache()
            _liveData.value = FavoritesState(recipes = recipesFromCache, favorites = getFavoritesId)

            val recipesFromBackend = recipesRepository.getRecipesByIds(getFavoritesId)

            if (recipesFromBackend == null) {
                _errorLiveData.value = "Ошибка получения данных"
                return@launch
            }

            val recipesWithFavorites = recipesFromBackend.map { recipe ->
                recipe.copy(isFavorite = getFavoritesId.contains(recipe.id))
            }

            recipesRepository.insertRecipesIntoCache(recipesWithFavorites)
            _liveData.value =
                FavoritesState(recipes = recipesWithFavorites, favorites = getFavoritesId)
        }
    }
}