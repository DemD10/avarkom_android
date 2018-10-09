package ru.arkell.avarkom.presentation.login_flow.sign_in

import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.request.signIn.SignInBody
import ru.arkell.avarkom.data.response.createToken.CreateTokenResponse
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.login_flow.BaseLoginPresenter
import javax.inject.Inject

class SignInPresenter @Inject constructor(
    var avarkomService: AvarkomService,
    var userProfileRepository: UserProfileStorage) : BaseLoginPresenter<SignInView>() {
  private var signInBody = SignInBody()

  fun signInData(): SignInBody = signInBody

  fun signIn() {
    userProfileRepository.clear()
    view?.showLoading()
    subscriptions?.add(avarkomService.signIn(signInBody)
        .compose(RxHelper.applySingleScheduler())
        .subscribe({
          activateUser(it)
        }, {
          view?.hideLoading()
          view?.showError()
        }))
  }

  private fun activateUser(it: CreateTokenResponse) {
    userProfileRepository.saveToken(it.token)
    subscriptions?.add(avarkomService.getUser(it.id)
        .compose(RxHelper.applySingleScheduler())
        .subscribe({ userProfile ->
          userProfileRepository.saveUserProfile(userProfile)
          userProfileRepository.saveToken(it.token)
          view?.hideLoading()
          view?.showMainScreen()
        }, {
          view?.hideLoading()
          view?.showError() }))
  }

  override fun clearUserData() {
    userProfileRepository.clear()
  }
}