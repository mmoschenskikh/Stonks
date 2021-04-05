package ru.maxultra.stonks.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.DetailsTabItemBinding
import ru.maxultra.stonks.databinding.FragmentStockDetailsBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.details.tabs.DetailsTabSelectionListener
import ru.maxultra.stonks.ui.details.tabs.DetailsTabsAdapter

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

        binding.viewPager.adapter = DetailsTabsAdapter(this)

        binding.tabLayout.addOnTabSelectedListener(DetailsTabSelectionListener())

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabBinding = DetailsTabItemBinding.inflate(layoutInflater)
            tabBinding.detailsTabText.text = getTabText(position)
            tab.customView = tabBinding.root
        }.attach()

        return root
    }

    private fun getTabText(position: Int) =
        when (position) {
            0 -> getString(R.string.chart)
            1 -> getString(R.string.summary)
            else -> throw IllegalArgumentException("No tab expected at position #$position")
        }
}
