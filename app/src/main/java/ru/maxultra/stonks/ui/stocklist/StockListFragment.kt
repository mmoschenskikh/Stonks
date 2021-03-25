package ru.maxultra.stonks.ui.stocklist

import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.databinding.FragmentStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class StockListFragment :
    BaseFragment<FragmentStockListBinding>(FragmentStockListBinding::inflate) {

    private val viewModelFactory by lazy { StockListViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<StockListViewModel> { viewModelFactory }

    companion object {
        fun newInstance() = StockListFragment()
    }
}
