package ru.arkell.avarkom.data.user_repository.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.data.user_repository.UserProfileStorageImpl

@Module
class UserProfileRepositoryModule(var context: Context) {
    @Provides
    fun provideUserRepository(): UserProfileStorage {
        return UserProfileStorageImpl(context)
    }
}
