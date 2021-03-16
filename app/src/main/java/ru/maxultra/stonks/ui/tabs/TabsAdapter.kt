package ru.maxultra.stonks.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.maxultra.stonks.ui.stocklist.StockListFragment

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = PAGE_COUNT

    override fun createFragment(position: Int) = StockListFragment.newInstance()

    companion object {
        private const val PAGE_COUNT = 2
    }
}
