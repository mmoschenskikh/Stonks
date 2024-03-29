package ru.maxultra.stonks.data.repository

import android.util.Log
import ru.maxultra.stonks.data.database.StockDao
import ru.maxultra.stonks.data.database.update
import ru.maxultra.stonks.data.network.FmpService

abstract class BaseRepository(
    private val stockDao: StockDao,
    private val fmpService: FmpService
) {
    /**
     * Toggles the "favourite" state.
     */
    suspend fun toggleFavourite(ticker: String) {
        val dbStock = stockDao.getStock(ticker)
        val newDbStock = dbStock.update(favourite = !dbStock.favourite)
        stockDao.update(newDbStock)
    }

    /**
     * Fetches stock profiles for [tickerList] from the remote API and updates database entries.
     * Some stocks may be discarded (considered as invalid) to provide better UX.
     */
    suspend fun updateProfiles(tickerList: List<String>) {
        Log.d("BaseRepository", "Updating ${tickerList.size} profiles")
        tickerList.chunked(50).forEach { chunk ->
            val query = chunk.joinToString()
            val response = fmpService.getProfile(query)
            response.forEach {
                if (it.ticker.length < 13
                    && it.companyName != null
                    && it.currentStockPrice != null
                    && it.currentStockPrice != 0.0
                    && it.currentStockPrice < 1000000
                    && it.diff != null
                    && it.diff.isFinite()
                    && it.currency != null
                ) {
                    val oldDbStock = stockDao.getStock(it.ticker)
                    val newDbStock = oldDbStock.update(
                        companyName = it.companyName,
                        logoUrl = it.logoUrl,
                        currency = it.currency,
                        currentPrice = it.currentStockPrice,
                        dayChange = it.diff,
                        description = it.description,
                        exchangeName = it.exchangeName,
                        sector = it.sector,
                        website = it.website
                    )
                    stockDao.update(newDbStock)
                } else {
                    stockDao.removeStock(it.ticker)
                }
            }
        }
    }
}
