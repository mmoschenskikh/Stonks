package ru.maxultra.stonks.ui.stocklist.generallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentGeneralStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.stocklist.recyclerview.StockListAdapter
import ru.maxultra.stonks.util.Status
import ru.maxultra.stonks.util.showNetworkErrorSnackBar

class GeneralStockListFragment :
    BaseFragment<FragmentGeneralStockListBinding>(FragmentGeneralStockListBinding::inflate) {

    private val adapter = StockListAdapter(::onItemClicked, ::onFavouriteClicked)
    private val viewModelFactory by lazy { GeneralStockListViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<GeneralStockListViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        binding.stockList.adapter = adapter

        viewModel.stocks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            if (it.isNullOrEmpty()) {
                binding.stockList.visibility = View.INVISIBLE
                binding.listEmptyText.visibility = View.VISIBLE
            } else {
                binding.listEmptyText.visibility = View.INVISIBLE
                binding.stockList.visibility = View.VISIBLE
            }
        }

        viewModel.stockListStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> binding.swipeRefresh.isRefreshing = true
                Status.SUCCESS -> binding.swipeRefresh.isRefreshing = false
                else -> {
                    binding.swipeRefresh.isRefreshing = false
                    showNetworkErrorSnackBar(binding.root)
                }
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getStockList()
        }

        return root
    }

    private fun onItemClicked(stock: Stock) = // TODO: Should open StockDetailsFragment
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            TabsFragmentDirections.actionTabsFragmentToStockCardFragment(
                stock.ticker
            )
        )
    }

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock.ticker)
}
