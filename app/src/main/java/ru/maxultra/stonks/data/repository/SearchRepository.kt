package ru.maxultra.stonks.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import ru.maxultra.stonks.data.database.*
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.FmpService
import ru.maxultra.stonks.data.network.asDatabaseModel
import ru.maxultra.stonks.data.network.asDomainModel

class SearchRepository(
    private val stockDao: StockDao,
    private val recentQueriesDao: RecentQueriesDao,
    private val fmpService: FmpService
) : BaseRepository(stockDao, fmpService) {
    suspend fun getPopularStocks(): List<Stock> =
        fmpService.getPopular()
            .filterNot { it.companyName.isNullOrBlank() }
            .sortedBy { it.companyName!!.length }
            .asDomainModel()

    fun getRecentQueries(): LiveData<List<Stock>> {
        val source = recentQueriesDao.get()
        return Transformations.map(source) { it.asDomainModel() }
    }

    suspend fun insertQuery(stock: Stock) {
        val query = stock.asRecentQuery()
        recentQueriesDao.deleteQuery(query.ticker)
        recentQueriesDao.insert(query)
    }

    suspend fun clearRecentQueries() = recentQueriesDao.clear()

    // FIXME
    fun search(query: String): LiveData<List<DatabaseStock>> = liveData {
        emit(emptyList())
        val searchResultList = fmpService.search(query)
            .filter { it.ticker.length < 13 && it.companyName != null }
        stockDao.insertAll(searchResultList.asDatabaseModel())
        val foundTickers = searchResultList.map { it.ticker }
        val source =
            Transformations.map(stockDao.getStocks()) { list -> list.filter { it.ticker in foundTickers } }
        emitSource(source)
        updateProfiles(foundTickers)
    }
}
