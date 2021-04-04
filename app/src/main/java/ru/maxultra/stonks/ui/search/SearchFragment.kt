package ru.maxultra.stonks.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.databinding.FragmentSearchBinding
import ru.maxultra.stonks.ui.base.BaseFragment
import ru.maxultra.stonks.ui.search.suggestionlist.SuggestionListAdapter
import ru.maxultra.stonks.util.Status
import ru.maxultra.stonks.util.showNetworkErrorSnackBar

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val adapterPopular = SuggestionListAdapter(::onItemClicked)
    private val adapterPrevious = SuggestionListAdapter(::onItemClicked)
    private val viewModelFactory by lazy { SearchViewModelFactory(requireContext()) }
    private val viewModel by activityViewModels<SearchViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.popularList.suggestionList.adapter = adapterPopular
        binding.previousSearchList.suggestionList.adapter = adapterPrevious

        viewModel.searchQuery.observe(viewLifecycleOwner) {
            val navController = findNavController()
            if (it.isNotBlank() && navController.currentDestination!!.id == R.id.searchFragment) {
                navController.navigate(R.id.action_searchFragment_to_searchResultFragment)
            }
        }

        viewModel.popularRequests.observe(viewLifecycleOwner) {
            adapterPopular.submitList(it)
        }

        viewModel.popularStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> {
                    binding.popularList.suggestionList.visibility = View.INVISIBLE
                    binding.popularEmptyText.visibility = View.INVISIBLE
                    binding.refreshButton.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.popularEmptyText.visibility = View.INVISIBLE
                    binding.refreshButton.visibility = View.VISIBLE
                    binding.popularList.suggestionList.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.popularList.suggestionList.visibility = View.INVISIBLE
                    binding.popularEmptyText.visibility = View.VISIBLE
                    binding.refreshButton.visibility = View.VISIBLE
                    showNetworkErrorSnackBar(binding.root)
                }
            }
        }

        viewModel.recentIsEmpty.observe(viewLifecycleOwner) {
            val visibility = when (it) {
                false -> View.VISIBLE
                else -> View.INVISIBLE
            }
            binding.recentTitle.visibility = visibility
            binding.clearRecentButton.visibility = visibility
            binding.previousSearchList.suggestionList.visibility = visibility
        }

        viewModel.recentRequests.observe(viewLifecycleOwner) {
            adapterPrevious.submitList(it)
        }

        binding.clearRecentButton.setOnClickListener { viewModel.clearRecent() }
        binding.refreshButton.setOnClickListener { viewModel.getPopularStocks() }

        return root
    }

    private fun onItemClicked(stock: Stock) =
        Toast.makeText(context, stock.ticker, Toast.LENGTH_SHORT).show()
}
