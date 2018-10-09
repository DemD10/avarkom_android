package ru.arkell.avarkom.presentation.main.tabs.car_accident.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(CarAccidentScreenModule::class)])
interface CarAccidentScreenComponent {
  fun inject(fragment: CarAccidentFragment)
}