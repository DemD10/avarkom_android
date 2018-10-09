package ru.arkell.avarkom.data.network.maps_api

import io.reactivex.Single
import javax.inject.Inject

// TODO Replace it by using user's city
private const val moscowCitySearchQuery: String = ""

class GeoCoderService @Inject constructor(val geoCoderApi: AvarkomGeoCoderApi) {
  fun getUserAddress(latLng: String): Single<GeoCoderResponse> {
    return geoCoderApi.getUserAddress(latLng)
  }

  fun getAddresses(lat: Double, lan: Double, apiToken: String): Single<AddressesResponse> {
    return geoCoderApi.getAddressFromLatLng("$lat ,$lan", apiToken, "ru")
  }

  fun getAddresses(query: String, apiToken: String): Single<AddressesResponse> {
    return geoCoderApi.getLatLngFromQuery(query + moscowCitySearchQuery, apiToken, "ru")
  }
}