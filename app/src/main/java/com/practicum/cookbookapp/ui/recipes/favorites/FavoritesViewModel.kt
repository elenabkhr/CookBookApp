package com.practicum.cookbookapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.AppExecutors
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.model.Recipe

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
        AppExecutors.threadPool.execute {
            val getFavorites = getFavorites().map { it.toInt() }.toSet()
            val recipes = recipesRepository.getRecipesByIds(getFavorites)

            if (recipes == null) {
                _errorLiveData.postValue("Ошибка получения данных")
                return@execute
            }

            _liveData.postValue(FavoritesState(favorites = getFavorites, recipes = recipes))
        }
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }
}