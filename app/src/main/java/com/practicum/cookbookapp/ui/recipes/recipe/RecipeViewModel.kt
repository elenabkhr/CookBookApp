package com.practicum.cookbookapp.ui.recipes.recipe

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
}