package ru.arkell.avarkom.presentation.login_flow.sign_in.di

import dagger.Component
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.presentation.login_flow.sign_in.SignInFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(SignInScreenModule::class)])
interface SignInScreenComponent {
  fun inject(fragment: SignInFragment)
}