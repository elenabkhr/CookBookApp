package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.AppExecutors
import com.practicum.cookbookapp.data.RecipesRepository
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val recipes: List<Recipe>? = emptyList(),
        val category: Category?,
        val categoryImage: Drawable?,
        val openRecipeId: Int? = null,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<RecipesListState>()
    val liveData: LiveData<RecipesListState> = _liveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val recipesRepository = RecipesRepository()
    private val appContext = getApplication<Application>()

    fun loadRecipesList(category: Category) {
        AppExecutors.threadPool.execute {
            val drawable = Drawable.createFromStream(
                appContext.assets.open(category.imageUrl),
                null
            )
            val recipes = recipesRepository.getRecipesByCategoryId(category.id)

            if (drawable == null || recipes == null) {
                Log.e("!!!", "Image not found")
                _errorLiveData.postValue("Ошибка получения данных")
                return@execute
            }

            _liveData.postValue(
                RecipesListState(category = category, categoryImage = drawable, recipes = recipes)
            )
        }
    }

    fun onRecipeClick(recipeId: Int) {
        _liveData.value = _liveData.value?.copy(openRecipeId = recipeId)
    }
}