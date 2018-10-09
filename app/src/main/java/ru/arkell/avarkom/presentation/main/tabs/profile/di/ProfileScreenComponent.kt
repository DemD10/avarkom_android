package ru.arkell.avarkom.presentation.main.tabs.profile.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.profile.ProfileFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(ProfileScreenModule::class)])
interface ProfileScreenComponent {
    fun inject(fragment: ProfileFragment)
}