package ru.maxultra.stonks.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.maxultra.stonks.ui.stocklist.StockListFragment
import ru.maxultra.stonks.ui.stocklist.StockListType

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        val type = when (position) {
            0 -> StockListType.LIST
            1 -> StockListType.FAVOURITE
            else -> throw IllegalArgumentException("No tab expected at position #$position")
        }
        return StockListFragment.newInstance(type)
    }

    companion object {
        private const val PAGE_COUNT = 2
    }
}
