package ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.ActivityScope
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SelectAddressActivity

@ActivityScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(SelectAddressScreenModule::class)])
interface SelectAddressScreenComponent {
  fun inject(activity: SelectAddressActivity)
}