package com.practicum.cookbookapp.di

import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}