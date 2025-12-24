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

    private val recipesRepository = RecipesRepository()

    fun loadFavorites() {
        viewModelScope.launch {
            val getFavorites = getFavorites().map { it.toInt() }.toSet()
            val recipes = recipesRepository.getRecipesByIds(getFavorites)

            if (recipes == null) {
                _errorLiveData.value = "Ошибка получения данных"
                return@launch
            }

            _liveData.value = FavoritesState(favorites = getFavorites, recipes = recipes)
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }
}