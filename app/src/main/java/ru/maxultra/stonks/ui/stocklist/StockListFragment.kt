package ru.maxultra.stonks.ui.stocklist

import ru.maxultra.stonks.databinding.FragmentStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class StockListFragment :
    BaseFragment<FragmentStockListBinding>(FragmentStockListBinding::inflate) {

    companion object {
        fun newInstance() = StockListFragment()
    }
}
