package com.practicum.cookbookapp.di

import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}