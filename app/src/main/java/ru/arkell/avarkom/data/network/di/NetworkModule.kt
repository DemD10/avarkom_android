package ru.arkell.avarkom.data.network.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.arkell.avarkom.BuildConfig
import ru.arkell.avarkom.data.network.api.AvarkomApi
import ru.arkell.avarkom.data.network.maps_api.AvarkomGeoCoderApi
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.data.network.service.AvarkomService
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.NANOSECONDS
import javax.inject.Named
import javax.inject.Singleton

private const val DEFAULT_CONNECT_TIMEOUT = 60000L
private val HTTP_LOG_LEVEL: HttpLoggingInterceptor.Level = if (BuildConfig.DEBUG) BODY else BASIC
private val BASE_URL =
    if (BuildConfig.DEBUG) "https://private-anon-ef15a64395-avarcomapi.apiary-proxy.com/api/"
    else "https://34.217.173.155:8000/api/"
private val BASE_GEOCODER_SERVICE_URL = "https://maps.googleapis.com/maps/api/"

@Module
class NetworkModule {
  @Provides
  fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
        .retryOnConnectionFailure(true)
        .readTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
        .connectionPool(ConnectionPool(0, 1, NANOSECONDS))
        .writeTimeout(DEFAULT_CONNECT_TIMEOUT, MILLISECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HTTP_LOG_LEVEL })
        .build()
  }

  @Provides
  @Singleton
  fun provideGson(): Gson {
    val gson = Gson()
    gson.serializeNulls()
    return gson
  }

  @Provides
  @Singleton
  fun provideAvarkomService(api: AvarkomApi, repo: UserProfileStorage): AvarkomService {
    return AvarkomService(api, repo)
  }

  @Provides
  @Singleton
  fun provideAvarkomApi(httpClient: OkHttpClient,
      gson: Gson): AvarkomApi {
    val retrofit = createRetrofit(httpClient, gson, BASE_URL)
    return retrofit.create(AvarkomApi::class.java)
  }

  @Provides
  @Named("BASE_URL")
  @Singleton
  fun provideBaseUrl(): String {
    return BASE_URL
  }

  @Provides
  @Singleton
  fun provideGeoCoderService(api: AvarkomGeoCoderApi): GeoCoderService {
    return GeoCoderService(api)
  }

  @Provides
  @Singleton
  fun provideGeoCoderApi(httpClient: OkHttpClient,
      gson: Gson): AvarkomGeoCoderApi {
    val retrofit = createRetrofit(httpClient, gson, BASE_GEOCODER_SERVICE_URL)
    return retrofit.create(AvarkomGeoCoderApi::class.java)
  }

  private fun createRetrofit(httpClient: OkHttpClient, gson: Gson, baseUrl: String): Retrofit {
    return Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient)
        .baseUrl(baseUrl)
        .build()
  }
}
