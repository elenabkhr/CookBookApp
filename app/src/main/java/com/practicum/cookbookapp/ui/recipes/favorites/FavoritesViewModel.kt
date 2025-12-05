package com.practicum.cookbookapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    data class FavoritesState(
        val recipes: List<Recipe> = emptyList(),
        val favorites: Set<Int> = emptySet(),
        val openRecipeId: Int? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<FavoritesState>()
    val liveData: LiveData<FavoritesState> = _liveData

    fun loadFavorites() {
        _liveData.value = FavoritesState(
            favorites = getFavorites().map { it.toInt() }.toSet(),
            recipes = STUB.getRecipesByIds(getFavorites().map { it.toInt() }.toSet())
        )
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