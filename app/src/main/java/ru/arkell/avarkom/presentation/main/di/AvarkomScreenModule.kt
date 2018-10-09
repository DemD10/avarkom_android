package ru.arkell.avarkom.presentation.main.di

import android.app.Activity
import dagger.Module
import dagger.Provides
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.di.ActivityScope
import ru.arkell.avarkom.presentation.main.AvarkomPresenter
import ru.arkell.avarkom.presentation.main.AvarkomRouter

@Module
@ActivityScope
class AvarkomScreenModule(var context: Activity) {
  @Provides
  @ActivityScope
  fun provideAvarkomPresenter(router: AvarkomRouter,
      userProfileStorage: UserProfileStorage)
      : AvarkomPresenter = AvarkomPresenter(router, userProfileStorage)

  @Provides
  @ActivityScope
  fun provideAvarkomRouter(): AvarkomRouter = AvarkomRouter(context)

}