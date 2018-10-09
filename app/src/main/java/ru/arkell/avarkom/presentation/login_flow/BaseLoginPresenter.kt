package ru.arkell.avarkom.presentation.login_flow

import ru.arkell.avarkom.presentation.base.BaseLCEView
import ru.arkell.avarkom.presentation.base.BasePresenter

abstract class BaseLoginPresenter<View> : BasePresenter<View>() {
  abstract fun clearUserData()
}