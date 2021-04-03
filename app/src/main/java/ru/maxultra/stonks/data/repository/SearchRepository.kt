package ru.maxultra.stonks.data.repository

import kotlinx.coroutines.flow.map
import ru.maxultra.stonks.data.database.RecentQueriesDao
import ru.maxultra.stonks.data.database.StockDao
import ru.maxultra.stonks.data.database.asDomainModel
import ru.maxultra.stonks.data.database.asRecentQuery
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.network.FmpService
import ru.maxultra.stonks.data.network.asDomainModel

class SearchRepository(
    private val stockDao: StockDao,
    private val recentQueriesDao: RecentQueriesDao,
    private val fmpService: FmpService
) {
    suspend fun getPopularStocks(): List<Stock> =
        fmpService.getPopular()
            .filterNot { it.companyName.isNullOrBlank() }
            .sortedBy { it.companyName!!.length }
            .asDomainModel()

    fun getRecentQueries() = recentQueriesDao.get()
        .map { recent -> recent.map { stockDao.getStock(it.ticker) }.asDomainModel() }

    suspend fun insertQuery(stock: Stock) {
        val query = stock.asRecentQuery()
        recentQueriesDao.deleteQuery(query.ticker)
        recentQueriesDao.insert(query)
    }

    suspend fun clearRecentQueries() = recentQueriesDao.clear()
}
