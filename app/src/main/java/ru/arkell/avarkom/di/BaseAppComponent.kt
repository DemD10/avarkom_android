package ru.arkell.avarkom.di

import android.content.Context
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ilyasavin on 12/4/17.
 */
@Component(modules = [(BaseAppModule::class)])
@Singleton
interface BaseAppComponent {
  fun provideContext(): Context
}
