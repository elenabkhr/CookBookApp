package com.practicum.cookbookapp.ui.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    data class CategoriesListState(
        val categories: List<Category>? = emptyList(),
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<CategoriesListState>()
    val liveData: LiveData<CategoriesListState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository(application.applicationContext)

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesFromCache = recipesRepository.getCategoriesFromCache()
            _liveData.value = CategoriesListState(categories = categoriesFromCache)

            val categoriesFromBackend = recipesRepository.getCategories()

            if (categoriesFromBackend != null) {
                recipesRepository.categoriesDao.insertAll(categoriesFromBackend)
                _liveData.value = CategoriesListState(categories = categoriesFromBackend)
            }
        }
    }
}