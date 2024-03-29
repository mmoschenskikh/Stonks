package ru.maxultra.stonks.ui.stocklist.favouritelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentFavouriteStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.stocklist.recyclerview.StockListAdapter
import ru.maxultra.stonks.ui.tabs.TabsFragmentDirections

class FavouriteStockListFragment :
    BaseFragment<FragmentFavouriteStockListBinding>(FragmentFavouriteStockListBinding::inflate) {

    private val adapter = StockListAdapter(::onItemClicked, ::onFavouriteClicked)
    private val viewModelFactory by lazy { FavouriteStockListViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<FavouriteStockListViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        binding.stockList.adapter = adapter

        viewModel.favourite.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.favouritesIsEmpty.observe(viewLifecycleOwner) {
            when (it) {
                false -> {
                    binding.listEmptyText.visibility = View.INVISIBLE
                    binding.stockList.visibility = View.VISIBLE
                }
                else -> {
                    binding.stockList.visibility = View.INVISIBLE
                    binding.listEmptyText.visibility = View.VISIBLE
                }
            }
        }

        return root
    }

    private fun onItemClicked(stock: Stock) = findNavController()
        .navigate(TabsFragmentDirections.actionTabsFragmentToStockCardFragment(stock.ticker))

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock.ticker)
}
