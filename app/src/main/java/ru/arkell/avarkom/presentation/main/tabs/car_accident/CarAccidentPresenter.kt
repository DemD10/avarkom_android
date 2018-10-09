package ru.arkell.avarkom.presentation.main.tabs.car_accident;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import com.google.android.gms.maps.model.LatLng
import ru.arkell.avarkom.data.network.maps_api.GEOCODER_KEY
import ru.arkell.avarkom.data.network.maps_api.GeoCoderService
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.presentation.base.BasePresenter
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_PERMISSIONS_REQUEST_CODE
import javax.inject.Inject

class CarAccidentPresenter @Inject constructor(private val locationService: CarAccidentService,
    private val geoCoderService: GeoCoderService) : BasePresenter<CarAccidentView>() {
  var lastLatLng: LatLng? = null

  @SuppressLint("MissingPermission")
  fun getLastLocation(latLng: LatLng) {

    lastLatLng = latLng
    locationService.setLastLocation(lastLatLng!!)
    locationService.getNewLocationPlacement()
  }


  fun getAddress(latLng: LatLng) {
    view?.showLoading()
    locationService.setLastLocation(latLng)

    geoCoderService.getAddresses(latLng.latitude, latLng.longitude, GEOCODER_KEY)
        .compose(RxHelper.applySingleScheduler())
        .subscribe({
          view?.hideLoading()
          if (it.results != null && it.results.isNotEmpty()) {
            var addressComponents = it.results?.first().addressComponents
            if (addressComponents != null) {
              var address = addressComponents[1].shortName + " " +
                  addressComponents?.first().shortName
              if (address.length > 24)
                address = addressComponents[1].shortName
              view?.showAddress(address)
            }
          }
        }, {
          view?.showError()
        })
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
}
