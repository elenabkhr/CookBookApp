package com.practicum.cookbookapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.cookbookapp.data.AppExecutors
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListState(
        val categories: List<Category>? = emptyList(),
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<CategoriesListState>()
    val liveData: LiveData<CategoriesListState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository()

    fun loadCategories() {
        AppExecutors.threadPool.execute {
            _liveData.postValue(CategoriesListState(categories = recipesRepository.getCategories()))
        }
    }
}