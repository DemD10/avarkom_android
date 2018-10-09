package ru.arkell.avarkom.data.network.di

import dagger.Component
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.data.user_repository.di.UserProfileRepositoryModule
import javax.inject.Singleton

@Component(modules = [(NetworkModule::class), UserProfileRepositoryModule::class])
@Singleton
interface PersistenceComponent {
  fun provideAvarkomService(): AvarkomService
  fun provdeGeocoderService(): GeoCoderService
  fun provideUserProfileRepository(): UserProfileStorage
}