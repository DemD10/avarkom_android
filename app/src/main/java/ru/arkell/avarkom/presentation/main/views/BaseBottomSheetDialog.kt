package ru.arkell.avarkom.presentation.main.views

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View

abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {
  abstract fun getLayoutId(): Int

  override fun setupDialog(dialog: Dialog, style: Int) {
    super.setupDialog(dialog, style)
    val contentView = View.inflate(context, getLayoutId(), null)
    dialog.setContentView(contentView)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
  }

}