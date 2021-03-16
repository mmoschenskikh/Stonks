package ru.maxultra.stonks.ui.tabs

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import ru.maxultra.stonks.R
import ru.maxultra.stonks.databinding.FragmentTabsBinding
import ru.maxultra.stonks.ui.base.BaseFragment

class TabsFragment : BaseFragment<FragmentTabsBinding>(FragmentTabsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = TabsAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getTabText(position)
        }.attach()
    }

    private fun getTabText(position: Int) =
        when (position) {
            0 -> getString(R.string.stocks)
            1 -> getString(R.string.favourite)
            else -> throw IllegalArgumentException("No tab expected at position #$position")
        }

    companion object {
        fun newInstance() = TabsFragment()
    }
}
