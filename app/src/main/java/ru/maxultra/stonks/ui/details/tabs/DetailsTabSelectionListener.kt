package ru.maxultra.stonks.ui.details.tabs

import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.google.android.material.tabs.TabLayout
import ru.maxultra.stonks.R

class DetailsTabSelectionListener :
    TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            val textView = it.view.findViewById<TextView>(R.id.detailsTabText)
            TextViewCompat.setTextAppearance(textView, R.style.DetailsTabSelected)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.let {
            val textView = it.view.findViewById<TextView>(R.id.detailsTabText)
            TextViewCompat.setTextAppearance(textView, R.style.DetailsTab)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        onTabSelected(tab)
    }
}
