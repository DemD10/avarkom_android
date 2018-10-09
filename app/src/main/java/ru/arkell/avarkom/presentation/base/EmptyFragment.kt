package ru.arkell.avarkom.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Temp empty fragment for non-implemented features
 */
class EmptyFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = View(activity)
    return view
  }

  override fun initViewElements() {
  }

  override fun initComponent() {
  }
}
