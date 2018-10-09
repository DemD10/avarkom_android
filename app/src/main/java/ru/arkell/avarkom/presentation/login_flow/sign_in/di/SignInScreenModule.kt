package ru.arkell.avarkom.presentation.login_flow.sign_in.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.presentation.login_flow.sign_in.SignInPresenter

@Module
@FragmentScope
class SignInScreenModule {
  @Provides
  @FragmentScope
  fun provideSignInPresenter(networkService: AvarkomService, userProfileStorage: UserProfileStorage)
      : SignInPresenter = SignInPresenter(networkService, userProfileStorage)
}