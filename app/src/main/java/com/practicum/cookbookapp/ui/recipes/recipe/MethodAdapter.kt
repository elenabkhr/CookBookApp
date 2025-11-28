package com.practicum.cookbookapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.databinding.ItemMethodBinding

class MethodAdapter(private var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    fun updateDataMethod(newMethod: List<String>) {
        dataSet = newMethod
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val method = binding.tvMethod
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemMethodBinding.inflate(inflater, viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.method.text = viewHolder.itemView.context.getString(
            R.string.string_method,
            position + 1,
            item
        )
    }

    override fun getItemCount() = dataSet.size
}