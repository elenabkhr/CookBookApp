package com.practicum.cookbookapp.di

import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}