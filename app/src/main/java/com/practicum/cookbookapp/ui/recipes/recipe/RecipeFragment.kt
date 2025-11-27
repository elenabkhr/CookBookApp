package com.practicum.cookbookapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.practicum.cookbookapp.databinding.FragmentRecipeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.fragment.app.activityViewModels
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.data.ARG_RECIPE

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    var recipeId = 0
    private val dataModel: RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initRecycler()

        recipeId = arguments?.getInt(ARG_RECIPE) ?: 0
        dataModel.loadRecipe(recipeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        dataModel.liveData.observe(viewLifecycleOwner) { state ->

            binding.tvRecipe.text = state.recipe?.title.toString()

            binding.ibHeart.setImageResource(
                if (state.isFavorite) (R.drawable.ic_heart)
                else (R.drawable.ic_heart_empty)
            )

            val drawable = try {
                Drawable.createFromStream(
                    state?.let { requireContext().assets.open(it.recipe?.imageUrl.toString()) },
                    null
                )
            } catch (e: Exception) {
                Log.e("!!!", "Image not found ${state?.recipe?.imageUrl}, $e")
                null
            }
            binding.imRecipe.setImageDrawable(drawable)

            binding.ibHeart.setOnClickListener {
                dataModel.onFavoritesClicked()
            }
        }
    }

    private fun initRecycler() {
        dataModel.liveData.observe(viewLifecycleOwner) { state ->

            val ingredientsAdapter = state.recipe?.let { IngredientsAdapter(it.ingredients) }
            binding.rvIngredients.adapter = ingredientsAdapter

            val ingredientsDivider =
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
                    dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                    dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_12)
                    dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_12)
                    isLastItemDecorated = false
                }
            binding.rvIngredients.addItemDecoration(ingredientsDivider)

            binding.rvMethod.adapter =
                state?.let { it.recipe?.let { it -> MethodAdapter(it.method) } }
            val methodDivider =
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
                    dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                    dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.padding_12)
                    dividerInsetStart = resources.getDimensionPixelSize(R.dimen.padding_12)
                    isLastItemDecorated = false
                }
            binding.rvMethod.addItemDecoration(methodDivider)

            binding.sbPortions.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.tvPortionsCount.text = progress.toString()
                    ingredientsAdapter?.updateIngredients(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}