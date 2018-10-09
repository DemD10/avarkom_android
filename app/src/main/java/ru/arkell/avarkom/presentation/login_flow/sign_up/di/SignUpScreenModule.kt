package ru.arkell.avarkom.presentation.login_flow.sign_up.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.presentation.login_flow.sign_up.SignUpPresenter

@Module
@FragmentScope
class SignUpScreenModule {
  @Provides
  @FragmentScope
  fun provideSignUpPresenter(networkService: AvarkomService, userProfileStorage: UserProfileStorage)
      : SignUpPresenter = SignUpPresenter(networkService, userProfileStorage)
}