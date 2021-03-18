package ru.maxultra.stonks.ui.tabs

import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import ru.maxultra.stonks.R

class TabSelectionListener(private val getTabText: (Int) -> String) :
    TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            val textViewSelected = it.view.findViewById<TextView>(R.id.tabTextSelected)
            val textViewUnselected = it.view.findViewById<TextView>(R.id.tabTextUnselected)
            textViewSelected.text = getTabText(tab.position)
            textViewUnselected.visibility = TextView.INVISIBLE
            textViewSelected.visibility = TextView.VISIBLE
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.let {
            val textViewSelected = it.view.findViewById<TextView>(R.id.tabTextSelected)
            val textViewUnselected = it.view.findViewById<TextView>(R.id.tabTextUnselected)
            textViewSelected.visibility = TextView.INVISIBLE
            textViewSelected.text = ""
            textViewUnselected.visibility = TextView.VISIBLE
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        onTabSelected(tab)
    }
}
