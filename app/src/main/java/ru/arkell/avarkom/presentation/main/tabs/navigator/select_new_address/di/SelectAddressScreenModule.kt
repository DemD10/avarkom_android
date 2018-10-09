package ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.di.ActivityScope
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SelectAddressPresenter

@Module
@ActivityScope
class SelectAddressScreenModule {
  @Provides
  @ActivityScope
  fun provideSelectAddressPresenter(geoCoderService: GeoCoderService, userProfileStorage: UserProfileStorage)
      : SelectAddressPresenter = SelectAddressPresenter(geoCoderService, userProfileStorage)
}