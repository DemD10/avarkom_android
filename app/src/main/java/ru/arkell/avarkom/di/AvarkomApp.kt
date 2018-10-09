package ru.arkell.avarkom.di

import ru.arkell.avarkom.data.network.di.PersistenceComponent


interface AvarkomApp {
  fun getAppComponent(): BaseAppComponent
  fun persistenceComponent(): PersistenceComponent
}