package ru.arkell.avarkom.presentation.main.tabs.navigator.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.arkell.avarkom.R
import ru.arkell.avarkom.presentation.main.views.BaseBottomSheetDialog

class NavigatorBottomDialog : BaseBottomSheetDialog() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun getLayoutId(): Int {
    return R.layout.fragment_navigator_dialog
  }
}