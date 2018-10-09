package ru.arkell.avarkom.data.user_repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import ru.arkell.avarkom.data.network.maps_api.ResultsItem
import ru.arkell.avarkom.data.response.signUp.UserProfile
import javax.inject.Inject


private const val PROFILE_PREFERENCES_NAME = "profile_preferences"
private const val FIELD_TOKEN = "token"
private const val FIELD_NAME = "name"
private const val FIELD_ID = "id"
private const val FIELD_USER_NAME = "user_name"
private const val FIELD_EMAIL = "email"
private const val FIELD_PHONE_NUMBER = "phone_number"
private const val FIELD_ADDRESSES_SEARCHES = "addresses_searches"

class UserProfileStorageImpl @Inject constructor(var context: Context) : UserProfileStorage {

  override fun getUserProfileObservable(): Single<UserProfile> {
    return Single.fromCallable { return@fromCallable getUserProfile() }
  }

  override fun getUserProfile(): UserProfile {
    val preferences = getProfileSharedPreferences()
    val token = getToken(preferences)
    val name = getName(preferences)
    val email = getEmail(preferences)
    val phone = getPhone(preferences)
    val userName = getUserName(preferences)
    return UserProfile(
        name = name,
        email = email,
        username = userName,
        phone = phone,
        token = token
    )
  }

  override fun saveUserProfile(userProfile: UserProfile) {
    val preferences = getProfileSharedPreferences()
    val editor = preferences.edit()
    editor.putString(FIELD_TOKEN, userProfile.token)
    editor.putString(FIELD_NAME, userProfile.name)
    editor.putString(FIELD_EMAIL, userProfile.email)
    editor.putString(FIELD_PHONE_NUMBER, userProfile.phone)
    editor.putString(FIELD_TOKEN, userProfile.token)
    editor.putInt(FIELD_ID, userProfile.id)
    editor.apply()
  }

  override fun saveToken(token: String) {
    val preferences = getProfileSharedPreferences()
    val editor = preferences.edit()
    editor.putString(FIELD_TOKEN, token)
    editor.apply()
  }

  override fun getToken(): String {
    val preferences = getProfileSharedPreferences()
    return "Token ${getToken(preferences)}"
  }

  private fun getProfileSharedPreferences(): SharedPreferences {
    return context.getSharedPreferences(PROFILE_PREFERENCES_NAME, Context.MODE_PRIVATE)
  }

  private fun getToken(preferences: SharedPreferences): String {
    return preferences.getString(FIELD_TOKEN, "")
  }

  private fun getName(preferences: SharedPreferences): String {
    return preferences.getString(FIELD_NAME, "")
  }

  private fun getPhone(preferences: SharedPreferences): String {
    return preferences.getString(FIELD_PHONE_NUMBER, "")
  }

  private fun getUserName(preferences: SharedPreferences): String {
    return preferences.getString(FIELD_USER_NAME, "")
  }

  private fun getEmail(preferences: SharedPreferences): String {
    return preferences.getString(FIELD_EMAIL, "")
  }

  override fun saveAddress(resultsItem: ResultsItem) {
    val preferences = getProfileSharedPreferences()
    val editor = preferences.edit()
    // first get all previous items and add current
    val gson: Gson = GsonBuilder()
        .create()
    val searchesList = getAddresses()
    if (searchesList.size >= 10) searchesList.removeAt(searchesList.size - 1)
    searchesList.add(resultsItem)

    editor.putString(FIELD_ADDRESSES_SEARCHES, gson.toJson(searchesList))
    editor.apply()

  }

  override fun getAddresses(): MutableList<ResultsItem> {
    val preferences = getProfileSharedPreferences()
    val gson: Gson = GsonBuilder()
        .create()
    val previousJson = preferences.getString(FIELD_ADDRESSES_SEARCHES, "")
    val searchesList: MutableList<ResultsItem> = gson.fromJson(previousJson,
        object : TypeToken<MutableList<ResultsItem>>() {}.type) ?: return mutableListOf()
    return searchesList
  }

  override fun clear() {
    val preferences = getProfileSharedPreferences()
    preferences.edit().clear().apply()
  }

}
