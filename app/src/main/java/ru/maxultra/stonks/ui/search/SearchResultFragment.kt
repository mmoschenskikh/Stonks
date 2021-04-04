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
import ru.maxultra.stonks.ui.stocklist.recyclerview.StockListAdapter
import ru.maxultra.stonks.util.Status
import ru.maxultra.stonks.util.showNetworkErrorSnackBar

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
        binding.list.stockList.adapter = adapter
        binding.list.listEmptyText.setText(R.string.nothing_found)

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            val navController = findNavController()
            if (it.isBlank() && navController.currentDestination!!.id == R.id.searchResultFragment) {
                navController.navigate(SearchResultFragmentDirections.actionSearchResultFragmentToSearchFragment())
            }
        }

        viewModel.searchResultList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.searchResult.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.list.stockList.visibility = View.INVISIBLE
                binding.list.listEmptyText.visibility = View.VISIBLE
            } else {
                binding.list.listEmptyText.visibility = View.INVISIBLE
                binding.list.stockList.visibility = View.VISIBLE
            }
        }

        viewModel.searchStatus.observe(viewLifecycleOwner) {
            binding.list.swipeRefresh.isRefreshing = false
            when (it) {
                Status.LOADING -> binding.list.swipeRefresh.isRefreshing = true
                Status.ERROR -> showNetworkErrorSnackBar(binding.root)
            }
        }
        binding.list.swipeRefresh.setOnRefreshListener {
            if (viewModel.searchStatus.value != Status.LOADING) viewModel.search()
        }
        return root
    }

    private fun onItemClicked(stock: Stock) { // TODO: Should open StockDetailsFragment
        viewModel.onStockClicked(stock)
        Toast.makeText(requireContext(), stock.ticker, Toast.LENGTH_SHORT).show()
    }

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock)

}
