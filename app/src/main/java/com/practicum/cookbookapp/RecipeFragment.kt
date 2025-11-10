package com.practicum.cookbookapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.practicum.cookbookapp.databinding.FragmentRecipeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentRecipeBinding must not be null"
        )

    var recipe: Recipe? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(ARG_RECIPE)
        }
        binding.tvRecipe.text = recipe?.title

        val drawable = try {
            Drawable.createFromStream(
                recipe?.imageUrl?.let { requireContext().assets.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("!!!", "Image not found ${recipe?.imageUrl}, $e")
            null
        }
        binding.imRecipe.setImageDrawable(drawable)
    }

    private fun initRecycler() {
        binding.rvIngredients.adapter = recipe?.ingredients?.let { IngredientsAdapter(it) }
        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_12)
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_12)
                isLastItemDecorated = false
            }
        binding.rvIngredients.addItemDecoration(ingredientsDivider)

        binding.rvMethod.adapter = recipe?.let { MethodAdapter(it.method) }
        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_12)
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_12)
                isLastItemDecorated = false
            }
        binding.rvMethod.addItemDecoration(methodDivider)
    }
}