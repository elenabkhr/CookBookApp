package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.cookbookapp.databinding.ItemRecipeBinding
import com.practicum.cookbookapp.model.Recipe

class RecipeListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.ivCardRecipe
        val titleTextView = binding.tvRecipeTitle
    }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binging = ItemRecipeBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binging)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        viewHolder.titleTextView.text = recipe.title

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.imageView.context.assets.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found ${recipe.imageUrl}, $e")
            null
        }
        viewHolder.imageView.setImageDrawable(drawable)

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size
}