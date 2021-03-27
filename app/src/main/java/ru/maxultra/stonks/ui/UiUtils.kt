package ru.maxultra.stonks.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.maxultra.stonks.R
import ru.maxultra.stonks.round
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun formatPrice(currency: Currency, price: Double): String =
    // Cannot use single instance of NumberFormat because of possible configuration change
    NumberFormat.getCurrencyInstance().also { it.currency = currency }.format(price)

fun formatPriceDelta(currency: Currency, price: Double, priceDelta: Double): String {
    val sign = when {
        priceDelta > 0 -> "+"
        priceDelta < 0 -> "-"
        else -> ""
    }
    val absDelta = abs(priceDelta)
    val change = NumberFormat.getCurrencyInstance().also { it.currency = currency }.format(absDelta)
    val percentChange = (absDelta / price * 100).round(2)
    return "$sign$change ($percentChange%)"
}

fun priceDeltaColor(priceDelta: Double) = when {
    priceDelta > 0 -> R.color.stock_raise
    priceDelta < 0 -> R.color.stock_fall
    else -> R.color.black
}
