package ru.arkell.avarkom.presentation.main.tabs.profile.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.profile.ProfilePresenter

@Module
@FragmentScope
class ProfileScreenModule {
    @Provides
    @FragmentScope
    fun provideProfilePresenter(
        networkService: AvarkomService,
        userProfileStorage: UserProfileStorage
    ) = ProfilePresenter(networkService, userProfileStorage)
}