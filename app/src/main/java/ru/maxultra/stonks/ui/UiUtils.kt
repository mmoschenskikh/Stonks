package ru.maxultra.stonks.ui

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.SearchBarBinding
import ru.maxultra.stonks.round
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs

/**
 * Provides desirable behaviour of search bar.
 */
fun SearchBarBinding.manageSearchBar(context: Context) {
    searchEditText.setOnFocusChangeListener { _, hasFocus ->
        val drawable = if (hasFocus) {
            R.drawable.ic_back_arrow
        } else {
            context.hideKeyboard(searchEditText)
            R.drawable.ic_search
        }
        leftIcon.setImageDrawable(ContextCompat.getDrawable(context, drawable))
    }

    searchEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

        override fun afterTextChanged(s: Editable?) {
            rightIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    })

    leftIcon.setOnClickListener {
        if (searchEditText.isFocused) {
            searchEditText.text = null
            searchEditText.clearFocus()
        }
    }

    rightIcon.setOnClickListener {
        searchEditText.text = null
    }
}

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
