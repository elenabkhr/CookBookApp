package com.practicum.cookbookapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Category
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListState(
        val categories: List<Category>? = emptyList(),
        val openCategory: Category? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<CategoriesListState>()
    val liveData: LiveData<CategoriesListState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository()
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    fun loadCategories() {
        threadPool.execute {
            _liveData.postValue(CategoriesListState(categories = recipesRepository.getCategories()))
        }
    }

    fun onCategoryClick(categoryId: Int) {
        threadPool.execute {
            val category = recipesRepository.getCategories()?.find { it.id == categoryId }

            if (category == null) {
                _errorLiveData.postValue("Ошибка получения данных")
                return@execute
            }

            _liveData.postValue(_liveData.value?.copy(openCategory = category))
        }
    }
}