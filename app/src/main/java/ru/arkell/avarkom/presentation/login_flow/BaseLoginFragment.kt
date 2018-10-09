package ru.arkell.avarkom.presentation.login_flow

import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import ru.arkell.avarkom.presentation.base.BaseFragment
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import java.util.concurrent.TimeUnit.MILLISECONDS

abstract class BaseLoginFragment : BaseFragment() {
  protected fun textChangeObservable(view: TextView) = RxTextView.textChangeEvents(view)
      .debounce(200, MILLISECONDS)

  protected fun setPhoneMask(phoneEditText: EditText) {
    val slots = UnderscoreDigitSlotsParser().parseSlots("___ ___ __ __")
    val formatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
    formatWatcher.installOn(phoneEditText)
  }
}