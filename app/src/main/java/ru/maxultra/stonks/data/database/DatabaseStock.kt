package ru.maxultra.stonks.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a stock entity in the database.
 */
@Entity
data class DatabaseStock(
    @PrimaryKey val ticker: String,
    val companyName: String,
    val logoUrl: String? = null,
    val currency: String? = null,
    val currentPrice: Double? = null,
    val previousClosePrice: Double? = null,
    val description: String? = null,
    val exchangeName: String? = null,
    val sector: String? = null,
    val website: String? = null,
    val priceMonth: String? = null,
    val priceYear: String? = null,
    val favourite: Boolean = false
)
