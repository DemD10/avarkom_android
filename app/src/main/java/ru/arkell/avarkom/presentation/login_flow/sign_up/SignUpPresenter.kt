package ru.arkell.avarkom.presentation.login_flow.sign_up

import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.request.signUp.UserBody
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.extensions.RxHelper.applySingleScheduler
import ru.arkell.avarkom.presentation.login_flow.BaseLoginPresenter
import javax.inject.Inject

class SignUpPresenter @Inject constructor(
    var avarkomService: AvarkomService,
    var userProfileRepository: UserProfileStorage) : BaseLoginPresenter<SignUpView>() {
  private var userBody = UserBody()

  fun user() = userBody

  fun signUp() {
    userProfileRepository.clear()
    view?.showLoading()
    subscriptions?.add(avarkomService.signUp(userBody)
        .compose(applySingleScheduler())
        .doAfterSuccess { view?.hideLoading() }
        .subscribe({ data ->
          view?.showConfirmationView(data.id, data.phone)
          userProfileRepository.saveUserProfile(data)
        }, {
          view?.hideLoading()
          view?.showError()
        }
        ))
  }

  override fun clearUserData() {
    userProfileRepository.clear()
  }
}