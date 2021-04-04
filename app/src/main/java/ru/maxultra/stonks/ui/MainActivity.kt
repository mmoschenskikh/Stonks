package ru.maxultra.stonks.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.appbar.AppBarLayout
import ru.maxultra.stonks.R
import ru.maxultra.stonks.data.model.StockDetails
import ru.maxultra.stonks.databinding.ActivityMainBinding
import ru.maxultra.stonks.ui.details.DetailsToolbarHandler
import ru.maxultra.stonks.ui.search.SearchViewModel
import ru.maxultra.stonks.ui.search.SearchViewModelFactory
import ru.maxultra.stonks.ui.search.clear
import ru.maxultra.stonks.ui.search.manageSearchBar
import ru.maxultra.stonks.ui.tabs.TabsFragmentDirections
import ru.maxultra.stonks.util.hideKeyboard

class MainActivity : AppCompatActivity(), DetailsToolbarHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModelFactory by lazy { SearchViewModelFactory(this) }
    private val searchViewModel by viewModels<SearchViewModel> { viewModelFactory }

    private var searchBarFocused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor = Color.BLACK
        }
        navController = findNavController(R.id.navHostFragment)

        binding.searchBar.manageSearchBar(this, searchViewModel)

        searchViewModel.navigateToSearchFragment.observe(this) {
            if (it == true) {
                if (navController.currentDestination!!.id == R.id.tabsFragment)
                    navController.navigate(TabsFragmentDirections.actionTabsFragmentToSearchFragment())
                searchViewModel.onNavigationToSearchFragmentFinished()
            }
        }

        searchViewModel.navigateUp.observe(this) {
            if (it == true) {
                navController.navigateUp()
                searchViewModel.onNavigationUpFinished()
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.tabsFragment) {
                binding.searchBar.clear()
            }
            val params = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
            if (destination.id == R.id.stockCardFragment) {
                searchBarFocused = binding.searchBar.searchEditText.isFocused
                binding.searchBar.root.visibility = View.GONE
                params.scrollFlags = 0
                binding.detailsTopNav.root.visibility = View.VISIBLE
            } else {
                binding.detailsTopNav.root.visibility = View.GONE
                binding.detailsTopNav.ticker.text = null
                binding.detailsTopNav.companyName.text = null
                params.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                binding.searchBar.root.visibility = View.VISIBLE
                if (searchBarFocused) binding.searchBar.searchEditText.requestFocus()
                searchBarFocused = false
            }
            binding.toolbar.layoutParams = params
        }

        binding.detailsTopNav.leftIcon.setOnClickListener {
            navController.navigateUp()
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null)
            hideKeyboard(binding.root)
        return super.dispatchTouchEvent(ev)
    }

    override fun setDetailsToolbarFields(stock: StockDetails) {
        binding.detailsTopNav.ticker.text = stock.ticker
        binding.detailsTopNav.companyName.text = stock.companyName
        binding.detailsTopNav.rightIcon.setOnClickListener {
            searchViewModel.onFavouriteClicked(stock.ticker)
        }
        binding.detailsTopNav.rightIcon.setImageResource(getStarImage(stock.favourite))
    }

    private fun getStarImage(favourite: Boolean) =
        if (favourite) R.drawable.star_yellow else R.drawable.star
}
