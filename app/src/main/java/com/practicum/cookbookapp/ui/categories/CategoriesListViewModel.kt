package com.practicum.cookbookapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListState(
        val category: List<Category>,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<CategoriesListState>()
    val liveData: LiveData<CategoriesListState> = _liveData

    fun loadCategories() {
        _liveData.value = CategoriesListState(category = STUB.getCategories())
    }
}