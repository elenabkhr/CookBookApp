package com.practicum.cookbookapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesState(
        val recipes: List<Recipe>? = emptyList(),
        val favorites: Set<Int> = emptySet(),
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<FavoritesState>()
    val liveData: LiveData<FavoritesState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository(application.applicationContext)

    fun loadFavorites() {
        viewModelScope.launch {
            val getFavoritesId = getFavorites().map { it.toInt() }.toSet()

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

            recipesRepository.recipesDao.insertRecipe(recipesFromBackend)
            _liveData.value =
                FavoritesState(recipes = recipesFromBackend, favorites = getFavoritesId)
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }
}