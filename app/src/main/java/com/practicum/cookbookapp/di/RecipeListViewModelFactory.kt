package com.practicum.cookbookapp.di

import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.ui.recipes.recipe_list.RecipesListViewModel

class RecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}