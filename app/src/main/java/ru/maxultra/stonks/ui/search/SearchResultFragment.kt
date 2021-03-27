package ru.maxultra.stonks.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.FragmentSearchResultBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class SearchResultFragment :
    BaseFragment<FragmentSearchResultBinding>(FragmentSearchResultBinding::inflate) {

    private val viewModel by activityViewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        viewModel.searchQuery.observe(viewLifecycleOwner) {
            val navController = findNavController()
            if (it.isBlank() && navController.currentDestination!!.id == R.id.searchResultFragment) {
                navController.navigate(R.id.action_searchResultFragment_to_searchFragment)
            }
        }
        return root
    }
}
