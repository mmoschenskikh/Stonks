package ru.maxultra.stonks.data.repository

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import ru.maxultra.stonks.data.database.StockDao
import ru.maxultra.stonks.data.database.asDomainModel
import ru.maxultra.stonks.data.database.asDomainProfile
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.model.StockDetails
import ru.maxultra.stonks.data.network.FmpService
import ru.maxultra.stonks.data.network.asDatabaseModel

class StockRepository(
    private val stockDao: StockDao,
    private val fmpService: FmpService
) : BaseRepository(stockDao, fmpService) {
    /**
     * Provides LiveData containing all the stocks from the database.
     */
    fun getAllStocks() = liveData<List<Stock>> {
        val source = Transformations.map(stockDao.getStocks()) { it.asDomainModel() }
        emitSource(source)
    }

    /**
     * Provides LiveData containing only favourite stocks.
     */
    fun getFavouriteStocks() = liveData<List<Stock>> {
        val source = Transformations.map(stockDao.getFavouriteStocks()) { it.asDomainModel() }
        emitSource(source)
    }

    fun getStock(ticker: String) = liveData<StockDetails> {
        val source =
            Transformations.map(stockDao.getStockAsLiveData(ticker)) { it.asDomainProfile() }
        emitSource(source)
    }

    /**
     * Fetches the stock list from the remote API and caches it in the database.
     * Some stocks may be discarded (considered as invalid) to provide better UX.
     */
    suspend fun fetchStockList() {
        val networkStockList = fmpService.getStocks()
            .filter { it.ticker.length < 13 && it.companyName != null }
        stockDao.insertAll(networkStockList.asDatabaseModel())
        val list = networkStockList.map { it.ticker }
        Log.d("StockRepository", "Updating profiles (${list.size})")
        updateProfiles(list)
    }
}
