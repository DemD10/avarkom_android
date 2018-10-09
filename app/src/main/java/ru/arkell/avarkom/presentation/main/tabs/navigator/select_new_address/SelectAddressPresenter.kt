package ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address

import com.google.android.gms.maps.model.LatLng
import ru.arkell.avarkom.data.network.maps_api.GEOCODER_KEY
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.data.network.maps_api.ResultsItem
import ru.arkell.avarkom.data.user_repository.UserProfileStorage
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.base.BasePresenter
import javax.inject.Inject

class SelectAddressPresenter @Inject constructor(
    var service: GeoCoderService, var userProfileStorage: UserProfileStorage) : BasePresenter<SelectAddressView>() {

  fun getAddressFromLocation(latLng: LatLng) {
    view?.showLoading()
    service.getAddresses(latLng.latitude, latLng.longitude, GEOCODER_KEY)
        .subscribe(
            { addressesResponse ->
              if (addressesResponse.results?.size!! > 0) {
                view?.showPlaceSuggestions(addressesResponse.results)
              } else {

              }
            },
            { errorResponse ->
              view?.showError()
            }
        )
  }

  fun getLocationFromQuery(query: String) {
    view?.showLoading()
    service.getAddresses(query, GEOCODER_KEY)
        .compose(RxHelper.applySingleScheduler())
        .subscribe(
            { addressesResponse ->
              if (addressesResponse.results?.isNotEmpty()!!) {
                view?.showPlaceSuggestions(addressesResponse.results)
                view?.hideLoading()
              } else {
              }
            },
            { errorResponse ->
              view?.showError()
            }
        )
  }

  fun saveSelectedAddress(resultsItem: ResultsItem){
    userProfileStorage.saveAddress(resultsItem)
  }

  fun getRecentSearches() {
    view?.showRecentSearches(userProfileStorage.getAddresses())
  }
}