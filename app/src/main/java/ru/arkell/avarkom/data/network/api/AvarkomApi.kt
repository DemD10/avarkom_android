package ru.arkell.avarkom.data.network.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.arkell.avarkom.data.AUTH_TOKEN
import ru.arkell.avarkom.data.request.activation.ActivationBody
import ru.arkell.avarkom.data.request.signIn.SignInBody
import ru.arkell.avarkom.data.request.signUp.UserBody
import ru.arkell.avarkom.data.response.activation.ActivationResponse
import ru.arkell.avarkom.data.response.createToken.CreateTokenResponse
import ru.arkell.avarkom.data.response.news.NewsResponse
import ru.arkell.avarkom.data.response.signUp.UserProfile

interface AvarkomApi {
  @POST("users/")
  fun signUp(@Body userData: UserBody): Single<UserProfile>

  @POST("token-auth/")
  fun signIn(@Body signInData: SignInBody): Single<CreateTokenResponse>

  @PATCH("users/{id}/activate/")
  fun activate(@Header(AUTH_TOKEN) header: String, @Path(
      "id") id: Int, @Body activationData: ActivationBody): Single<ActivationResponse>

  @GET("users/{id}")
  fun getUser(@Header(AUTH_TOKEN) header: String, @Path("id") id: Int): Single<UserProfile>

  @GET("news/")
  fun getNews(@Header(AUTH_TOKEN) header: String, @Query("lat") lat: String, @Query(
      "lng") lng: String, @Query("limit") limit: String, @Query(
      "offset") offset: String): Single<NewsResponse>

}