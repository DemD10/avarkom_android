package ru.arkell.avarkom.presentation.login_flow.confirmation.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.presentation.login_flow.confirmation.ConfirmationPresenter

@Module
@FragmentScope
class ConfirmationScreenModule {
  @Provides
  @FragmentScope
  fun provideConfirmationPresenter(networkService: AvarkomService)
      : ConfirmationPresenter = ConfirmationPresenter(networkService)
}