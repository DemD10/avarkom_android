package ru.arkell.avarkom.presentation.main.tabs.news.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.news.NewsPresenter

@Module
@FragmentScope
class NewsScreenModule {
  @Provides
  @FragmentScope
  fun provideConfirmationPresenter(networkService: AvarkomService)
      : NewsPresenter = NewsPresenter(networkService)
}