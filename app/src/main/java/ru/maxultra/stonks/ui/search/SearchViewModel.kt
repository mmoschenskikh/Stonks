package ru.maxultra.stonks.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _navigateToSearchFragment = MutableLiveData(false)
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    fun onNavigateToSearchFragment() {
        _navigateToSearchFragment.value = true
    }

    fun onNavigationToSearchFragmentFinished() {
        _navigateToSearchFragment.value = false
    }

    fun onNavigateUp() {
        _navigateUp.value = true
    }

    fun onNavigationUpFinished() {
        _navigateUp.value = false
    }
}
