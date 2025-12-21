package com.practicum.cookbookapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.model.Recipe
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesState(
        val recipes: List<Recipe>? = emptyList(),
        val favorites: Set<Int> = emptySet(),
        val openRecipeId: Int? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<FavoritesState>()
    val liveData: LiveData<FavoritesState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository()
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    fun loadFavorites() {
        threadPool.execute {
            val getFavorites = getFavorites().map { it.toInt() }.toSet()
            val recipes = recipesRepository.getRecipesByIds(getFavorites)

            if (recipes == null) {
                _errorLiveData.postValue("Ошибка получения данных")
                return@execute
            }

            _liveData.postValue(FavoritesState(favorites = getFavorites, recipes = recipes))
        }
    }

    fun onRecipeClick(recipeId: Int) {
        _liveData.value = _liveData.value?.copy(openRecipeId = recipeId)
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }
}