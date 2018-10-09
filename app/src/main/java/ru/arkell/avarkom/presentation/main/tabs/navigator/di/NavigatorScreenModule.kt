package ru.arkell.avarkom.presentation.main.tabs.navigator.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorFragment
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorPresenter
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.LocationService
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.LocationServiceImpl

@Module
@FragmentScope
class NavigatorScreenModule(private var navigatorFragment: NavigatorFragment) {
  @Provides
  @FragmentScope
  fun provideNavigatorPresenter(service: LocationService, geoCoderService: GeoCoderService)
      : NavigatorPresenter = NavigatorPresenter(service, geoCoderService)

  @Provides
  @FragmentScope
  fun provideCarAccidentService(): LocationService = LocationServiceImpl(navigatorFragment)
}