package ru.maxultra.stonks.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.databinding.FragmentStockDetailsBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class StockDetailsFragment :
    BaseFragment<FragmentStockDetailsBinding>(FragmentStockDetailsBinding::inflate) {

    private val viewModelFactory by lazy { DetailsViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<DetailsViewModel> { viewModelFactory }
    private lateinit var activity: DetailsToolbarHandler
    private lateinit var args: StockDetailsFragmentArgs

    override fun onAttach(context: Context) {
        super.onAttach(context)
        args = StockDetailsFragmentArgs.fromBundle(requireArguments())
        activity = requireActivity() as DetailsToolbarHandler
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        viewModel.setTicker(args.ticker)

        viewModel.stock.observe(viewLifecycleOwner) {
            activity.setDetailsToolbarFields(it)
        }

        return root
    }
}
