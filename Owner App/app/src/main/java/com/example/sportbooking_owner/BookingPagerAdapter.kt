package com.example.sportbooking_owner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookingPagerAdapter (frg: FragmentActivity): FragmentStateAdapter(frg) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return IncomingBookingFragment()
            1 -> return FinishBookingFragment()
            else -> return Fragment()
        }
    }
}