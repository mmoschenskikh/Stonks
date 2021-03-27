package ru.maxultra.stonks.ui.search

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.SearchBarBinding
import ru.maxultra.stonks.ui.hideKeyboard


/**
 * Provides desirable behaviour of search bar.
 */
fun SearchBarBinding.manageSearchBar(context: Context, viewModel: SearchViewModel) {
    searchEditText.setOnFocusChangeListener { _, hasFocus ->
        val drawable = if (hasFocus) {
            viewModel.onNavigateToSearchFragment()
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
            viewModel.updateQuery(s?.toString() ?: "")
            rightIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    })

    leftIcon.setOnClickListener {
        if (searchEditText.isFocused) {
            clear()
            viewModel.onNavigateUp()
        }
    }

    rightIcon.setOnClickListener {
        searchEditText.text = null
    }
}

fun SearchBarBinding.clear() {
    searchEditText.text = null
    searchEditText.clearFocus()
}
