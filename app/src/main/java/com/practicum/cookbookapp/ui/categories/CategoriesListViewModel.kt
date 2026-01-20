package com.practicum.cookbookapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    data class CategoriesListState(
        val categories: List<Category>? = emptyList(),
        val isLoading: Boolean = false,
    )

    private val _categoriesState = MutableLiveData<CategoriesListState>()
    val categoriesState: LiveData<CategoriesListState> = _categoriesState

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromCache = recipesRepository.getCategoriesFromCache()
            _categoriesState.value = CategoriesListState(categories = categoriesFromCache)

            val categoriesFromBackend = recipesRepository.getCategories()

            if (categoriesFromBackend != null) {
                recipesRepository.insertCategoriesIntoCache(categoriesFromBackend)
                _categoriesState.value = CategoriesListState(categories = categoriesFromBackend)
            }
        }
    }
}