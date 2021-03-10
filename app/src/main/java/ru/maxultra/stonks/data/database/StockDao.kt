package ru.maxultra.stonks.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Interface for database access.
 */
@Dao
interface StockDao {

    @Query("SELECT * FROM databasestock")
    suspend fun getStocks(): LiveData<List<DatabaseStock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: List<DatabaseStock>)
}
