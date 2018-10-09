package ru.arkell.avarkom.presentation.main;

import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.presentation.base.BasePresenter
import javax.inject.Inject

class AvarkomPresenter @Inject constructor(var router: AvarkomRouter,
    var userProfileStorage: UserProfileStorage) : BasePresenter<AvarkomView>() {
  fun checkIfAuthorized() {
    val user = userProfileStorage.getUserProfile()
    val ifAuthorized: Boolean = user.token.isNullOrEmpty()
    if (ifAuthorized) {
      router.startLoginScreen()
    } else {
      view?.showAvarkomMainView()
    }
  }

}
