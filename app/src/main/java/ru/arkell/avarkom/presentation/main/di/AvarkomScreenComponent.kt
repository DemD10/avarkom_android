package ru.arkell.avarkom.presentation.main.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.ActivityScope
import ru.arkell.avarkom.presentation.main.AvarkomActivity

@ActivityScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(AvarkomScreenModule::class)])
interface AvarkomScreenComponent {
  fun inject(avarkomActivity: AvarkomActivity)
}