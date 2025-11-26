package com.practicum.cookbookapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.model.Ingredient

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val isLoading: Boolean = false,
        val imageUrl: String? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String> = emptyList(),
        val isFavorite: Boolean = false,
        val portions: Int = 1,
    )

    private val liveDataMutable = MutableLiveData<RecipeState>()
    val liveData: LiveData<RecipeState> = liveDataMutable

    private val appContext = getApplication<Application>()

    private var recipeId = 0
    private val favorites = getFavorites().contains(recipeId.toString())

    init {
        liveDataMutable.value = RecipeState(isFavorite = favorites)
    }

    fun loadRecipe(id: Int) {
        //TODO("load from network")

        this.recipeId = id
        val isFav = getFavorites().contains(id.toString())

        liveDataMutable.value = RecipeState(
            isFavorite = isFav,
            portions = liveDataMutable.value?.portions ?: 1,
        )
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()

        val isFav = if (favorites.contains(recipeId.toString())) {
            favorites.remove(recipeId.toString())
            false
        } else {
            favorites.add(recipeId.toString())
            true
        }

        saveFavorites(favorites)
        liveDataMutable.value = liveDataMutable.value?.copy(isFavorite = isFav)
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = appContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val setString = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
        return HashSet(setString)
    }

    private fun saveFavorites(setStringId: Set<String>) {
        val sharedPrefs = appContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit {
            putStringSet(FAVORITES_KEY, setStringId)
        }
    }
}