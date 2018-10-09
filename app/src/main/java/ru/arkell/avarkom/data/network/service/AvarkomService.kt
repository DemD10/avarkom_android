package ru.arkell.avarkom.data.network.service

import io.reactivex.Single
import ru.arkell.avarkom.data.network.api.AvarkomApi
import ru.arkell.avarkom.data.request.activation.ActivationBody
import ru.arkell.avarkom.data.request.signIn.SignInBody
import ru.arkell.avarkom.data.request.signUp.UserBody
import ru.arkell.avarkom.data.response.activation.ActivationResponse
import ru.arkell.avarkom.data.response.createToken.CreateTokenResponse
import ru.arkell.avarkom.data.response.news.NewsItem
import ru.arkell.avarkom.data.response.signUp.UserProfile
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import javax.inject.Inject

class AvarkomService @Inject constructor(private val api: AvarkomApi,
    private val repo: UserProfileStorage) {
  fun signUp(userData: UserBody): Single<UserProfile> {
    return api.signUp(userData)
  }

  fun signIn(signInBody: SignInBody): Single<CreateTokenResponse> {
    return api.signIn(signInBody)
  }

  fun activate(id: Int, activationBody: ActivationBody): Single<ActivationResponse> {
    return api.activate(repo.getToken(), id, activationBody)
  }

  fun getUser(userId: Int): Single<UserProfile> {
    val token = repo.getToken()
    return api.getUser(token, userId)
  }

  fun getNews(latLng: String, limit: Int, offset: Int): Single<List<NewsItem>> {
    return api.getNews(repo.getToken(),
        latLng, latLng, limit.toString(), offset.toString()).map { it.results }
  }
}