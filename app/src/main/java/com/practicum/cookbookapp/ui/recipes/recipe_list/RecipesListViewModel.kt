package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.model.Category
import com.practicum.cookbookapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListState(
        val listRecipes: List<Recipe> = emptyList(),
        val category: Category?,
        val categoryImage: Drawable?,
        val isLoading: Boolean = false,
    )

    private val _liveData = MutableLiveData<RecipesListState>()
    val liveData: LiveData<RecipesListState> = _liveData

    private val appContext = getApplication<Application>()

    fun loadRecipesList(id: Int) {
        val drawable = try {
            Drawable.createFromStream(
                STUB.getCategories()
                    .find { it.id == id }?.imageUrl?.let { appContext.assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found $e")
            null
        }

        _liveData.value = RecipesListState(
            category = STUB.getCategories().find { it.id == id },
            categoryImage = drawable,
            listRecipes = STUB.getRecipesByCategoryId(id)
        )
    }
}