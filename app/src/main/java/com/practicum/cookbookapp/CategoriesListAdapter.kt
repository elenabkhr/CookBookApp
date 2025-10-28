package com.practicum.cookbookapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.cookbookapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.ivCardCategories
        val titleTextView = binding.tvCategoriesTitle
        val descriptionTextView = binding.tvCategoriesDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.imageView.context.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found ${category.imageUrl}, $e")
            null
        }
        viewHolder.imageView.setImageDrawable(drawable)
    }

    override fun getItemCount() = dataSet.size
}