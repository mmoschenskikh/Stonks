package ru.maxultra.stonks.ui.details.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.maxultra.stonks.ui.details.ChartFragment
import ru.maxultra.stonks.ui.details.SummaryFragment

class DetailsTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = PAGE_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChartFragment()
            1 -> SummaryFragment()
            else -> throw IllegalArgumentException("No tab expected at position #$position")
        }
    }

    companion object {
        private const val PAGE_COUNT = 2
    }
}
