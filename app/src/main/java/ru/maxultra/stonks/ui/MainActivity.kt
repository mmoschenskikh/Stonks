package ru.maxultra.stonks.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.ActivityMainBinding
import ru.maxultra.stonks.ui.search.SearchViewModel
import ru.maxultra.stonks.ui.search.SearchViewModelFactory
import ru.maxultra.stonks.ui.search.clear
import ru.maxultra.stonks.ui.search.manageSearchBar
import ru.maxultra.stonks.ui.tabs.TabsFragmentDirections
import ru.maxultra.stonks.util.hideKeyboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModelFactory by lazy { SearchViewModelFactory(this) }
    private val searchViewModel by viewModels<SearchViewModel> { viewModelFactory }

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
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null)
            hideKeyboard(binding.root)
        return super.dispatchTouchEvent(ev)
    }
}
