package ru.maxultra.stonks.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.FragmentSummaryBinding
import ru.maxultra.stonks.databinding.SummaryBlockBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class SummaryFragment : BaseFragment<FragmentSummaryBinding>(FragmentSummaryBinding::inflate) {

    private val viewModelFactory by lazy { DetailsViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<DetailsViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        binding.companyName.title.text = getString(R.string.company_name)
        binding.sector.title.text = getString(R.string.sector)
        binding.website.title.text = getString(R.string.website)
        binding.exchange.title.text = getString(R.string.exchange)
        binding.description.title.text = getString(R.string.description)

        viewModel.stock.observe(viewLifecycleOwner) {
            if (it != null) {
                setBlockFields(binding.companyName, it.companyName)
                setBlockFields(binding.sector, it.sector)
                setBlockFields(binding.website, it.website)
                setBlockFields(binding.exchange, it.exchangeName)
                setBlockFields(binding.description, it.description)
            }
        }

        return root
    }

    private fun setBlockFields(block: SummaryBlockBinding, content: String?) {
        if (content.isNullOrBlank()) {
            block.root.visibility = View.GONE
            block.content.text = null
        } else {
            block.root.visibility = View.VISIBLE
            block.content.text = content
        }
    }
}
