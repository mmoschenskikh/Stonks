package ru.maxultra.stonks.data.repository

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay
import ru.maxultra.stonks.data.adapter
import ru.maxultra.stonks.data.database.StockDao
import ru.maxultra.stonks.data.database.asDomainModel
import ru.maxultra.stonks.data.database.asDomainProfile
import ru.maxultra.stonks.data.database.update
import ru.maxultra.stonks.data.model.ChartPoint
import ru.maxultra.stonks.data.model.Stock
import ru.maxultra.stonks.data.model.StockDetails
import ru.maxultra.stonks.data.network.FmpService
import ru.maxultra.stonks.data.network.asDatabaseModel
import java.text.SimpleDateFormat
import java.util.*

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

    suspend fun fetchStock(ticker: String) {
        val profile = fmpService.getProfile(ticker)[0]
        if (stockDao.stockExists(ticker)) {
            val oldDbStock = stockDao.getStock(ticker)
            val newDbStock = oldDbStock.update(
                companyName = profile.companyName,
                logoUrl = profile.logoUrl,
                currency = profile.currency,
                currentPrice = profile.currentStockPrice,
                dayChange = profile.diff,
                description = profile.description,
                exchangeName = profile.exchangeName,
                sector = profile.sector,
                website = profile.website
            )
            stockDao.update(newDbStock)
        } else {
            stockDao.insertAll(listOf(profile.asDatabaseModel()))
        }
    }

    suspend fun fetchThirtyMinChart(ticker: String) {
        val data = fmpService.getThirtyMinChart(ticker)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val chart = data.map {
            val parse = dateFormat.parse(it.date)!!.time
            ChartPoint(parse, it.price.toFloat())
        }
        val json = adapter.toJson(chart)
        val oldDbStock = stockDao.getStock(ticker)
        val newDbStock = oldDbStock.update(
            priceMonth = json
        )
        stockDao.update(newDbStock)
    }
}
