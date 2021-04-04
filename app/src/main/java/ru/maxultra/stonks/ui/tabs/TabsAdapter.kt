package ru.maxultra.stonks.ui.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.maxultra.stonks.ui.stocklist.favouritelist.FavouriteStockListFragment
import ru.maxultra.stonks.ui.stocklist.generallist.GeneralStockListFragment

class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GeneralStockListFragment()
            1 -> FavouriteStockListFragment()
            else -> throw IllegalArgumentException("No tab expected at position #$position")
        }
    }

    companion object {
        private const val PAGE_COUNT = 2
    }
}
