package com.practicum.cookbookapp.ui.recipes.recipe_list

import com.practicum.cookbookapp.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.cookbookapp.data.URL_RECIPES
import com.practicum.cookbookapp.databinding.ItemRecipeBinding
import com.practicum.cookbookapp.model.Recipe

class RecipeListAdapter(private var dataSet: List<Recipe>) :
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

    fun updateListRecipes(listRecipe: List<Recipe>) {
        dataSet = listRecipe
        notifyDataSetChanged()
    }

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

        Glide
            .with(viewHolder.itemView)
            .load(("${URL_RECIPES}images/${recipe.imageUrl}"))
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.imageView)

        viewHolder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size
}