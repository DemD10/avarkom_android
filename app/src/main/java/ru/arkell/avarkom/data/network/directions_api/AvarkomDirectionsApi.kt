package ru.arkell.avarkom.data.network.directions_api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.arkell.avarkom.data.network.maps_api.GeoCoderResponse

const val DESTINATIONS_KEY = "AIzaSyAZn6gmqwz7abCHj7z6fPyp9X-vR1oc7gI"

interface AvarkomDirectionsApi {
  @GET("directions/json")
  fun getRoute(@Query("latlng") latLng: String) : Single<GeoCoderResponse>
}