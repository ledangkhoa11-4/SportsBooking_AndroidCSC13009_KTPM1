package com.example.sportbooking_owner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView


class LockableScrollView : ScrollView {

    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    private var mScrollable = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setScrollingEnabled(enabled: Boolean) {
        mScrollable = enabled
    }

    fun isScrollable(): Boolean {
        return mScrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // if we can scroll pass the event to the superclass
                return mScrollable && super.onTouchEvent(ev)
            }
            else -> return super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return mScrollable && super.onInterceptTouchEvent(ev)
    }
}