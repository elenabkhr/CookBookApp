package com.practicum.cookbookapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.cookbookapp.databinding.ItemIngredientsBinding

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val quantityUnitOfMeasure = binding.tvQuantityUnitOfMeasure
        val description = binding.tvIngredientsDescription
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemIngredientsBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        viewHolder.description.text = ingredient.description

        viewHolder.quantityUnitOfMeasure.text = viewHolder.itemView.context.getString(
            R.string.quantity_unit_of_measure,
            ingredient.quantity,
            ingredient.unitOfMeasure
        )
    }

    override fun getItemCount() = dataSet.size
}