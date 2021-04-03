package ru.maxultra.stonks.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentQueriesDao {

    @Query("SELECT * FROM searchquery")
    fun get(): Flow<List<SearchQuery>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: SearchQuery)

    @Query("DELETE FROM searchquery WHERE ticker = :ticker")
    suspend fun deleteQuery(ticker: String)

    @Query("DELETE FROM searchquery")
    suspend fun clear()
}
