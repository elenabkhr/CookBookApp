package com.practicum.cookbookapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.cookbookapp.databinding.ItemIngredientsBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var quantity: Int = 1

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

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

        val newQuantity = BigDecimal(ingredient.quantity) * BigDecimal(quantity.toString())
        val formatQuantity = if (newQuantity.stripTrailingZeros().scale() <= 0) {
            newQuantity.toBigInteger().toString()
        } else {
            newQuantity.setScale(1, RoundingMode.HALF_UP).toPlainString()
        }

        viewHolder.description.text = ingredient.description
        viewHolder.quantityUnitOfMeasure.text = viewHolder.itemView.context.getString(
            R.string.quantity_unit_of_measure,
            formatQuantity,
            ingredient.unitOfMeasure
        )
    }

    override fun getItemCount() = dataSet.size
}