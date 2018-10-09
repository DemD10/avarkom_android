package ru.arkell.avarkom.presentation.login_flow.confirmation

import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.request.activation.ActivationBody
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.base.BasePresenter
import javax.inject.Inject

class ConfirmationPresenter @Inject constructor(
    var avarkomService: AvarkomService) : BasePresenter<ConfirmationView>() {
  fun confirmActivation(userId: Int, smsCode: Int) {
    avarkomService.activate(userId, ActivationBody(smsCode))
        .compose(RxHelper.applySingleScheduler())
        .subscribe({ response ->
          when (response.status) {
            200 -> view?.endLoginFlow()
            else -> view?.showActivationError(response.toString())
          }
        }, { view?.showActivationError(it.localizedMessage) })
  }
}