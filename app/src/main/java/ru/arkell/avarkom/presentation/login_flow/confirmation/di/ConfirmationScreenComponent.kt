package ru.arkell.avarkom.presentation.login_flow.confirmation.di

import dagger.Component
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.presentation.login_flow.confirmation.ConfirmationFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(ConfirmationScreenModule::class)])
interface ConfirmationScreenComponent {
  fun inject(fragment: ConfirmationFragment)
}