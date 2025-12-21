package com.practicum.cookbookapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.practicum.cookbookapp.databinding.FragmentListCategoriesBinding
import com.practicum.cookbookapp.model.Category

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
            state.categories?.let { categoriesListAdapter.updateCategoriesList(it) }
            state.openCategory?.let { category ->
                openRecipesByCategoryId(category)
            }
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    private fun openRecipesByCategoryId(category: Category) {
        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        )
    }
}