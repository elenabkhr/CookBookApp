package com.practicum.cookbookapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.ui.recipes.recipe_list.RecipeListAdapter
import com.practicum.cookbookapp.data.ARG_RECIPE
import com.practicum.cookbookapp.databinding.FragmentFavoritesBinding
import kotlin.getValue

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentFavoritesBinding must not be null"
        )

    private val viewModel: FavoritesViewModel by activityViewModels()
    private lateinit var favoritesAdapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeState()
        viewModel.loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            if (state.favorites.isEmpty()) binding.rvFavorites.isVisible = false
            else binding.tvStub.isVisible = false
            favoritesAdapter.updateListRecipes(state.recipes)
            state.openRecipeId?.let { openRecipeByRecipeId(it) }
        }
    }

    private fun initUI() {
        favoritesAdapter = RecipeListAdapter(emptyList())
        binding.rvFavorites.adapter = favoritesAdapter

        favoritesAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                viewModel.onRecipeClick(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE, recipeId)
        }

        findNavController().navigate(R.id.recipeFragment, bundle)
    }
}