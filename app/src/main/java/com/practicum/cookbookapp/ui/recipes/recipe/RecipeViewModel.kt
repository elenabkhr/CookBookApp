package com.practicum.cookbookapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.cookbookapp.model.Ingredient

class RecipeViewModel() : ViewModel() {

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

    init {
        Log.i("!!!", "isFavorite = false")
        liveDataMutable.value = RecipeState(isFavorite = false)
    }
}