package com.practicum.cookbookapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListState(
        val categories: List<Category> = emptyList(),
        val openCategoryId: Category? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<CategoriesListState>()
    val liveData: LiveData<CategoriesListState> = _liveData

    fun loadCategories() {
        _liveData.value = CategoriesListState(categories = STUB.getCategories())
    }

    fun onCategoryClick(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }
            ?: throw IllegalArgumentException("Category with id:$categoryId not found")
        _liveData.value = _liveData.value?.copy(openCategoryId = category)
    }
}