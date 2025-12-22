package com.practicum.cookbookapp.ui.recipes.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.practicum.cookbookapp.databinding.FragmentRecipeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.practicum.cookbookapp.R

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

    private val args: RecipeFragmentArgs by navArgs()
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter

    private val viewModel: RecipeViewModel by activityViewModels()

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

        val recipeId = args.recipeId

        initUI()
        initListeners()
        observeState()

        viewModel.loadRecipe(recipeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            binding.tvRecipe.text = state.recipe?.title ?: ""

            binding.ibHeart.setImageResource(
                if (state.isFavorite) (R.drawable.ic_heart)
                else (R.drawable.ic_heart_empty)
            )

            binding.imRecipe.setImageDrawable(state.recipeImage)

            ingredientsAdapter.updateDataIngredients(
                state.recipe?.ingredients ?: emptyList(),
                state.portionsCount
            )
            methodAdapter.updateDataMethod(state.recipe?.method ?: emptyList())
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initUI() {
        ingredientsAdapter = IngredientsAdapter(emptyList())
        binding.rvIngredients.adapter = ingredientsAdapter

        methodAdapter = MethodAdapter(emptyList())
        binding.rvMethod.adapter = methodAdapter

        val divider =
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
        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
    }

    private fun initListeners() {
        binding.ibHeart.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        binding.sbPortions.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                binding.tvPortionsCount.text = progress.toString()
                viewModel.updatePortionsCount(progress)
            }
        )
    }
}