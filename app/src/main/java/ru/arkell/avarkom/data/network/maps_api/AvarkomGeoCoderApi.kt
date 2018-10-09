package ru.arkell.avarkom.data.network.maps_api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val GEOCODER_KEY = "AIzaSyCqMgjpsOq9B51rbYkFZk7iaM_a5_jd524"

interface AvarkomGeoCoderApi {
    @GET("geocode/json?sensor=true&&key=AIzaSyCqMgjpsOq9B51rbYkFZk7iaM_a5_jd524")
    fun getUserAddress(@Query("latlng") latLng: String) : Single<GeoCoderResponse>

    @GET("geocode/json")
    fun getAddressFromLatLng(@Query("latlng") lat: String,
        @Query("key") apiKey: String, @Query("language") language: String): Single<AddressesResponse>

    @GET("geocode/json")
    fun getLatLngFromQuery(@Query("address") lat: String,
        @Query("key") apiKey: String, @Query("language") language: String): Single<AddressesResponse>
}