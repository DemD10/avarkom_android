package ru.arkell.avarkom.presentation.main.tabs.profile

import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.presentation.base.BasePresenter
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    var networkService: AvarkomService,
    var userProfileStorage: UserProfileStorage
) : BasePresenter<ProfileView>() {

    fun onViewCreated() {
        val profile = userProfileStorage.getUserProfile()

        view?.showUserName(profile.name)
        view?.showUserNumber(profile.phone)

        view?.showProfit("80 000")
        view?.showFeedbackCount("132")
        view?.showSuccessfulCount("119")
    }
}