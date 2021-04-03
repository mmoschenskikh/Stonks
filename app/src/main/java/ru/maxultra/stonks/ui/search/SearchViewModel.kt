package ru.maxultra.stonks.ui.search

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.maxultra.stonks.data.database.StockDatabase
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.StonksNetwork
import ru.maxultra.stonks.data.repository.SearchRepository
import ru.maxultra.stonks.data.repository.StockRepository
import ru.maxultra.stonks.util.Status
import java.util.*

class SearchViewModel(
    private val stockRepository: StockRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _searchResult = MutableLiveData<List<Stock>>()
    val searchResult: LiveData<List<Stock>>
        get() = _searchResult

    private val placeholder = Stock("", "")
    private val placeholderList = listOf(placeholder, placeholder)

    private val _popularRequests = MutableLiveData(placeholderList)
    val popularRequests: LiveData<List<Stock>>
        get() = _popularRequests

    private val _popularStatus = MutableLiveData(Status.LOADING)
    val popularStatus: LiveData<Status>
        get() = _popularStatus

    val recentRequests = searchRepository.getRecentQueries()
    val recentIsEmpty = Transformations.map(recentRequests) { it.isEmpty() }

    private val _navigateToSearchFragment = MutableLiveData(false)
    val navigateToSearchFragment: LiveData<Boolean>
        get() = _navigateToSearchFragment

    private val _navigateUp = MutableLiveData(false)
    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private var searchJob: Job? = null

    init {
        getPopularStocks()
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        search()
    }

    fun search() {
        searchJob = viewModelScope.launch {
            searchQuery.value?.let {
                if (it.isNotBlank()) _searchResult.postValue(stockRepository.search(it))
            }
        }
    }

    fun clearRecent() = viewModelScope.launch { searchRepository.clearRecentQueries() }

    fun getPopularStocks() = viewModelScope.launch {
        try {
            _popularStatus.value = Status.LOADING
            _popularRequests.value = placeholderList
            _popularRequests.value = searchRepository.getPopularStocks()
            _popularStatus.value = Status.SUCCESS
        } catch (e: Exception) {
            _popularStatus.value = Status.ERROR
            _popularRequests.value = placeholderList
        }
    }

    fun onStockClicked(stock: Stock) = viewModelScope.launch {
        searchRepository.insertQuery(stock)
    }

    fun onFavouriteClicked(stock: Stock) = viewModelScope.launch {
        stockRepository.toggleFavourite(stock)
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

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val stockDao = StockDatabase.getDatabase(context).stockDao
            val recentQueriesDao = StockDatabase.getDatabase(context).recentQueriesDao
            val service = StonksNetwork.service
            val stockRepository = StockRepository(stockDao, service)
            val searchRepository = SearchRepository(stockDao, recentQueriesDao, service)
            val viewModel = SearchViewModel(stockRepository, searchRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
