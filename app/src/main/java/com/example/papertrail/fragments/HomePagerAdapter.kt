package com.example.papertrail.fragments

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
//            1 -> EntriesFragment()
//            2 -> UserFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
