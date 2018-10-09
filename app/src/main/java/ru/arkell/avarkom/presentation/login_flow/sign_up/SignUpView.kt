package ru.arkell.avarkom.presentation.login_flow.sign_up

import ru.arkell.avarkom.presentation.base.BaseLCEView

interface SignUpView : BaseLCEView {
  fun showConfirmationView(userId: Int, phone: String)
}