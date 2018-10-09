package ru.arkell.avarkom.presentation.login_flow.confirmation

import ru.arkell.avarkom.presentation.base.BaseLCEView

interface ConfirmationView : BaseLCEView {
  fun endLoginFlow()
  fun showActivationError(message: String)
}