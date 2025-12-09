package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.practicum.cookbookapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentListRecipesBinding must not be null"
        )

    private val args: RecipesListFragmentArgs by navArgs()
    private lateinit var recipeListAdapter: RecipeListAdapter
    private val viewModel: RecipesListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = args.categoryId
        initUI()
        observeState()
        viewModel.loadRecipesList(categoryId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            binding.tvCategory.text = state.category?.title ?: ""
            binding.imCategory.setImageDrawable(state.categoryImage)
            recipeListAdapter.updateListRecipes(state.recipes)
            state.openRecipeId?.let { openRecipeByRecipes(it) }
        }
    }

    private fun initUI() {
        recipeListAdapter = RecipeListAdapter(emptyList())
        binding.rvCategory.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                viewModel.onRecipeClick(recipeId)
            }
        })
    }

    private fun openRecipeByRecipes(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}