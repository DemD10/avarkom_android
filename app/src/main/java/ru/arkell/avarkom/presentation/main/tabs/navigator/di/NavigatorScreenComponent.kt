package ru.arkell.avarkom.presentation.main.tabs.navigator.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(NavigatorScreenModule::class)])
interface NavigatorScreenComponent {
  fun inject(fragment: NavigatorFragment)
}