package ru.maxultra.stonks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A database for the app that stores stocks info.
 */
@Database(entities = [DatabaseStock::class], version = 1, exportSchema = false)
abstract class StockDatabase : RoomDatabase() {
    abstract val stockDao: StockDao

    companion object {
        private const val DATABASE_NAME = "stocks"
        private lateinit var INSTANCE: StockDatabase

        /**
         * Provides an instance of database.
         */
        fun getDatabase(context: Context): StockDatabase {
            synchronized(StockDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            StockDatabase::class.java,
                            DATABASE_NAME
                        ).build()
                }
            }
            return INSTANCE
        }
    }
}
