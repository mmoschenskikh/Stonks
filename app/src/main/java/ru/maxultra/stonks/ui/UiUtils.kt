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
