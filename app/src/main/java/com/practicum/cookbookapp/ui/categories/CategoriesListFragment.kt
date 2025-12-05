package com.practicum.cookbookapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.practicum.cookbookapp.R
import com.practicum.cookbookapp.ui.recipes.recipe_list.RecipesListFragment
import com.practicum.cookbookapp.data.ARG_CATEGORY_ID
import com.practicum.cookbookapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException(
            "Binding for FragmentListCategoriesBinding must not be null"
        )

    private lateinit var categoriesListAdapter: CategoriesListAdapter
    private val viewModel: CategoriesListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeState()
        viewModel.loadCategories()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.updateCategoriesList(state.categories)
            state.openCategoryId?.let { id ->
                openRecipesByCategoryId(id)
            }
        }
    }

    private fun initUI() {
        categoriesListAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                viewModel.onCategoryClick(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val bundle = Bundle()
        bundle.putInt(ARG_CATEGORY_ID, categoryId)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<RecipesListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}