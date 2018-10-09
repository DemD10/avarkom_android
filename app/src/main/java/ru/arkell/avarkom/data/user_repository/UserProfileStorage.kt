package ru.arkell.avarkom.data.user_repository

import io.reactivex.Single
import ru.arkell.avarkom.data.network.maps_api.ResultsItem
import ru.arkell.avarkom.data.response.signUp.UserProfile

interface UserProfileStorage {
  fun getUserProfileObservable(): Single<UserProfile>
  fun getUserProfile(): UserProfile
  fun saveUserProfile(userProfile: UserProfile)
  fun saveToken(token: String)
  fun getToken(): String
  fun clear()
  fun getAddresses(): MutableList<ResultsItem>
  fun saveAddress(resultsItem: ResultsItem)
}
