package ru.arkell.avarkom.presentation.main.tabs.car_accident.di

import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentFragment
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentPresenter
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentService
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentServiceImpl

@Module
@FragmentScope
class CarAccidentScreenModule(private var carAccidentFragment: CarAccidentFragment) {
  @Provides
  @FragmentScope
  fun provideCarAccidentPresenter(service: CarAccidentService, geoCoderService: GeoCoderService)
      : CarAccidentPresenter = CarAccidentPresenter(service, geoCoderService)

  @Provides
  @FragmentScope
  fun provideCarAccidentService(): CarAccidentService = CarAccidentServiceImpl(carAccidentFragment)
}