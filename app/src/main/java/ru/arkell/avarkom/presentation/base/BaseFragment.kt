package ru.arkell.avarkom.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initComponent()
  }

  abstract fun initViewElements()
  abstract fun initComponent()
}