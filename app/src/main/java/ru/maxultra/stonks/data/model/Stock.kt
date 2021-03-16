package ru.maxultra.stonks.data.model

import java.util.*

data class Stock(
    val ticker: String,
    val companyName: String,
    var logoUrl: String? = null,
    var currency: Currency? = null,
    var currentStockPrice: Int? = null,
    var previousClosePrice: Int? = null,
    var favourite: Boolean = false
)
