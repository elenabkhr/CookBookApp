package com.practicum.cookbookapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.FAVORITES_KEY
import com.practicum.cookbookapp.data.SP_NAME
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipeState(
        val recipe: Recipe? = null,
        val isLoading: Boolean = false,
        val isFavorite: Boolean = false,
        val portionsCount: Int = 1,
        val recipeImage: Drawable?,
    )

    private val _liveData = MutableLiveData<RecipeState>()
    val liveData: LiveData<RecipeState> = _liveData

    private val appContext = getApplication<Application>()

    fun loadRecipe(id: Int) {
        //TODO("load from network")

        val drawable = try {
            Drawable.createFromStream(
                STUB.getRecipeById(id)?.imageUrl?.let { appContext.assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found $e")
            null
        }

        _liveData.value = RecipeState(
            recipe = STUB.getRecipeById(id),
            isFavorite = getFavorites().contains(id.toString()),
            portionsCount = _liveData.value?.portionsCount ?: 1,
            recipeImage = drawable
        )
    }

    fun onFavoritesClicked() {
        val recipeId = _liveData.value?.recipe?.id
        val setFavorites = getFavorites()
        val isFavorite = setFavorites.contains(recipeId.toString())

        if (isFavorite) {
            setFavorites.remove(recipeId.toString())
        } else {
            setFavorites.add(recipeId.toString())
        }
        saveFavorites(setFavorites)

        _liveData.value = _liveData.value?.copy(isFavorite = !isFavorite)
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