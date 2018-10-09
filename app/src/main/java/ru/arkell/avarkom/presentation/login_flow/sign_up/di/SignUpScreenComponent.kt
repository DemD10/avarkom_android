package ru.arkell.avarkom.presentation.login_flow.sign_up.di

import dagger.Component
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.presentation.login_flow.sign_up.SignUpFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(SignUpScreenModule::class)])
interface SignUpScreenComponent {
  fun inject(fragment: SignUpFragment)
}