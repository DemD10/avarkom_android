package ru.arkell.avarkom.presentation.main.views

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * A viewpager controller with disabled swipes and transition animation
 */
class NonSwipeableViewPager : ViewPager {

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return false
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    return false
  }

  override fun arrowScroll(direction: Int): Boolean {
    return false
  }

  override fun setCurrentItem(item: Int) {
    super.setCurrentItem(item, false)
  }
}
