package ru.arkell.avarkom.presentation.main.tabs.news.di

import dagger.Component
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.di.FragmentScope
import ru.arkell.avarkom.presentation.main.tabs.news.NewsFragment

@FragmentScope
@Component(dependencies = [(PersistenceComponent::class)],
    modules = [(NewsScreenModule::class)])
interface NewsScreenComponent {
  fun inject(fragment: NewsFragment)
}