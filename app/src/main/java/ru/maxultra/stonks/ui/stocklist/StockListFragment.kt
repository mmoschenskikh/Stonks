package ru.maxultra.stonks.ui.stocklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class StockListFragment :
    BaseFragment<FragmentStockListBinding>(FragmentStockListBinding::inflate) {

    private val adapter = StockListAdapter(::onItemClicked, ::onFavouriteClicked)
    private val viewModelFactory by lazy { StockListViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<StockListViewModel> { viewModelFactory }

    private lateinit var listType: StockListType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { listType = it.get(ARG_LIST_TYPE) as StockListType }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.stockList.adapter = adapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listToObserve = when (listType) {
            StockListType.LIST -> viewModel.stocks
            StockListType.FAVOURITE -> viewModel.favourite
        }
        listToObserve.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onItemClicked(stock: Stock) = // TODO: Should open StockDetailsFragment
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()

    private fun onFavouriteClicked(stock: Stock) = viewModel.onFavouriteClicked(stock)

    companion object {
        private const val ARG_LIST_TYPE = "StockListFragment_Type"

        fun newInstance(type: StockListType) =
            StockListFragment().apply {
                arguments = bundleOf(ARG_LIST_TYPE to type)
            }
    }
}

enum class StockListType {
    LIST, FAVOURITE
}
