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
import ru.maxultra.stonks.databinding.FragmentSearchResultBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.stocklist.StockListAdapter

class SearchResultFragment :
    BaseFragment<FragmentSearchResultBinding>(FragmentSearchResultBinding::inflate) {

    private val adapter = StockListAdapter(::onItemClicked, ::onFavouriteClicked)
    private val viewModelFactory by lazy { SearchViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<SearchViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.searchResultList.stockList.adapter = adapter

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            val navController = findNavController()
            if (it.isBlank() && navController.currentDestination!!.id == R.id.searchResultFragment) {
                navController.navigate(R.id.action_searchResultFragment_to_searchFragment)
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.searchResult.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onItemClicked(stock: Stock) = // TODO: Should open StockDetailsFragment
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock)

}
