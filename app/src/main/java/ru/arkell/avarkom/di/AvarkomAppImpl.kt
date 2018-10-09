package ru.arkell.avarkom.di

import android.app.Application
import ru.arkell.avarkom.data.network.di.DaggerPersistenceComponent
import ru.arkell.avarkom.data.network.di.NetworkModule
import ru.arkell.avarkom.data.network.di.PersistenceComponent
import ru.arkell.avarkom.data.user_repository.di.UserProfileRepositoryModule

class AvarkomAppImpl : Application(), AvarkomApp {
  private val persistenceComponent: PersistenceComponent by lazy {
    DaggerPersistenceComponent.builder()
        .networkModule(NetworkModule())
        .userProfileRepositoryModule(UserProfileRepositoryModule(this))
        .build()
  }

  private val baseAppComponent: BaseAppComponent by lazy {
    DaggerBaseAppComponent.builder()
        .baseAppModule(BaseAppModule(applicationContext))
        .build()
  }

  override fun getAppComponent(): BaseAppComponent = baseAppComponent
  override fun persistenceComponent(): PersistenceComponent = persistenceComponent
}