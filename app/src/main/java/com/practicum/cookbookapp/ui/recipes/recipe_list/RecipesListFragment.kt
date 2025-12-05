package com.practicum.cookbookapp.ui.recipes.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.ui.recipes.recipe.RecipeFragment
import com.practicum.cookbookapp.data.ARG_CATEGORY_ID
import com.practicum.cookbookapp.data.ARG_RECIPE
import com.practicum.cookbookapp.data.STUB
import com.practicum.cookbookapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentListRecipesBinding must not be null"
        )

    var categoryId: Int = 0
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

        categoryId = requireArguments().getInt(ARG_CATEGORY_ID)
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
            recipeListAdapter.updateListRecipes(state.listRecipes)
        }
    }

    private fun initUI() {
        recipeListAdapter = RecipeListAdapter(emptyList())
        binding.rvCategory.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipes(recipeId)
            }
        })
    }

    private fun openRecipeByRecipes(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle()
        recipe?.let { bundle.putInt(ARG_RECIPE, it.id) }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}