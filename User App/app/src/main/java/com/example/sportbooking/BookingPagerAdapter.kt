package com.example.sportbooking

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookingPagerAdapter (frg: FragmentActivity): FragmentStateAdapter(frg) {
    override fun getItemCount(): Int {
        return 3
    }
    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return IncomingBookingFragment()
            1 -> return FinishBookingFragment()
            2 -> return AllBookingFragment()
            else -> return Fragment()
        }
    }
}