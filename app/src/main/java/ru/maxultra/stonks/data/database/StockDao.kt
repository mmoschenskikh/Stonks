package ru.maxultra.stonks.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface for database access.
 */
@Dao
interface StockDao {

    @Query("SELECT * FROM databasestock")
    fun getStocks(): LiveData<List<DatabaseStock>>

    @Query("SELECT * FROM databasestock WHERE favourite = 1")
    fun getFavouriteStocks(): LiveData<List<DatabaseStock>>

    @Query("SELECT * FROM databasestock WHERE ticker = :ticker LIMIT 1")
    fun getStockAsLiveData(ticker: String): LiveData<DatabaseStock>

    @Query("SELECT * FROM databasestock WHERE ticker = :ticker LIMIT 1")
    suspend fun getStock(ticker: String): DatabaseStock

    @Query("SELECT EXISTS(SELECT * FROM databasestock WHERE ticker = :ticker)")
    suspend fun stockExists(ticker: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(stocks: List<DatabaseStock>)

    @Update
    suspend fun update(stock: DatabaseStock)

    @Query("DELETE FROM databasestock WHERE ticker = :ticker")
    suspend fun removeStock(ticker: String)
}
