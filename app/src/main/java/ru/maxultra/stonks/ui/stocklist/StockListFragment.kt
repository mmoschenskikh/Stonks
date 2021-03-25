package ru.maxultra.stonks.ui.stocklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.databinding.FragmentStockListBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class StockListFragment :
    BaseFragment<FragmentStockListBinding>(FragmentStockListBinding::inflate) {

    private val viewModelFactory by lazy { StockListViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<StockListViewModel> { viewModelFactory }
    private lateinit var listType: StockListType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        arguments?.let { listType = it.get(ARG_LIST_TYPE) as StockListType }
        return root
    }

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
