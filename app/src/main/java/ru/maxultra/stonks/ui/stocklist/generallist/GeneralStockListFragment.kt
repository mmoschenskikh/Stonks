package ru.maxultra.stonks.ui.stocklist.generallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentGeneralStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.stocklist.recyclerview.StockListAdapter
import ru.maxultra.stonks.util.Status
import ru.maxultra.stonks.util.hideKeyboard

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
        }

        viewModel.stockListStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> {
                    binding.stockList.visibility = View.INVISIBLE
                    binding.listEmptyText.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.listEmptyText.visibility = View.INVISIBLE
                    binding.stockList.visibility = View.VISIBLE
                }
                else -> {
                    if (adapter.itemCount == 0) {
                        binding.stockList.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.listEmptyText.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.listEmptyText.visibility = View.INVISIBLE
                        binding.stockList.visibility = View.VISIBLE
                    }
                    requireContext().hideKeyboard(binding.root)
                    showErrorSnackbar()
                }
            }
        }

        return root
    }

    fun showErrorSnackbar() {
        Snackbar.make(binding.root, R.string.connection_error_snackbar, Snackbar.LENGTH_LONG).show()
    }

    private fun onItemClicked(stock: Stock) = // TODO: Should open StockDetailsFragment
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock)
}
