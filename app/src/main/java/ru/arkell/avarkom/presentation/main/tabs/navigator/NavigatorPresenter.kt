package ru.arkell.avarkom.presentation.main.tabs.navigator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import com.google.android.gms.maps.model.LatLng
import ru.arkell.avarkom.data.network.maps_api.AddressComponentsItem
import ru.arkell.avarkom.data.network.maps_api.GEOCODER_KEY
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.base.BasePresenter
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.LocationService
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_PERMISSIONS_REQUEST_CODE
import javax.inject.Inject

class NavigatorPresenter @Inject constructor(private val locationService: LocationService,
    private val geoCoderService: GeoCoderService) : BasePresenter<NavigatorView>() {
  private var lastLatLng: LatLng? = null

  var locationLatLng: LatLng? = null
  var destinationLatLng: LatLng? = null

  @SuppressLint("MissingPermission")
  fun getLastLocation(latLng: LatLng) {

    lastLatLng = latLng
    locationService.setLastLocation(lastLatLng!!)
    locationService.renderMapMarker()
  }

  fun onRequestPermissionsResult(context: Activity, requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      when {
        (grantResults.first() == PackageManager.PERMISSION_GRANTED) -> {
          locationService.checkGPSOn()
        }
        else -> {
          view?.showSnackBar(View.OnClickListener {
            locationService.startLocationPermissionRequest()
          })
        }
      }
    }
  }

  fun getAddress(placeSearchFlag: Int, isFromPicker: Boolean, latLng: LatLng) {
    view?.showLoading()

    geoCoderService.getAddresses(latLng.latitude, latLng.longitude, GEOCODER_KEY)
        .compose(RxHelper.applySingleScheduler())
        .subscribe({
          view?.hideLoading()
          if (it.results != null && it.results.isNotEmpty()) {
            val addressComponents = it.results.first().addressComponents
            if (addressComponents != null) {
              var address = extractAddressString(addressComponents)
              if (placeSearchFlag == REQUEST_CODE_MY_PLACE_SEARCH)
                view?.showAddress(address, isFromPicker)
              else view?.showDestinationAddress(address, isFromPicker)

              if (placeSearchFlag == REQUEST_CODE_MY_PLACE_SEARCH) locationLatLng = latLng
              else destinationLatLng = latLng
            }
          }
        }, {
          view?.showError()
        })
  }

  private fun extractAddressString(
      addressComponents: List<AddressComponentsItem>): String {
    var address = addressComponents[1].shortName + " " +
        addressComponents?.first().shortName
    if (address.length > 24)
      address = addressComponents[1].shortName
    return address
  }


  fun checkIfDestinationsFilled() {
    if (destinationLatLng != null && locationLatLng != null) {
      Log.e("LATLNG",
          ("checking " + locationLatLng.toString() + "/" + destinationLatLng.toString())
      )
      view?.showRoute(locationLatLng!!, destinationLatLng!!)

    }
  }

  fun switchDestinations() {
    if (destinationLatLng != null && locationLatLng != null) {
      val toast = "before " + locationLatLng.toString() + "/" + destinationLatLng.toString()+ "\n"
      val newMyLocation: LatLng? = LatLng(destinationLatLng!!.latitude,
          destinationLatLng!!.longitude)
      val newDestinationLocation = LatLng(locationLatLng!!.latitude, locationLatLng!!.longitude)
      locationLatLng = newMyLocation
      destinationLatLng = newDestinationLocation
      Log.e("LATLNG",
          toast.plus("after " + locationLatLng.toString() + "/" + destinationLatLng.toString())
      )
      view?.switchDestinations()


    }
  }
}