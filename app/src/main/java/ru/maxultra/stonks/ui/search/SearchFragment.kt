package ru.maxultra.stonks.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentSearchBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val adapterPopular = SuggestionListAdapter(::onItemClicked)
    private val adapterPrevious = SuggestionListAdapter(::onItemClicked)
    private val viewModelFactory by lazy { SearchViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<SearchViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.popularList.suggestionList.adapter = adapterPopular
        binding.previousSearchList.suggestionList.adapter = adapterPrevious

        viewModel.getPopularStocks()

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.popularRequests.observe(viewLifecycleOwner) {
            adapterPopular.submitList(it)
        }
        viewModel.recentRequests.observe(viewLifecycleOwner) {
            adapterPrevious.submitList(it)
        }
    }

    private fun onItemClicked(stock: Stock) =
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()
}
