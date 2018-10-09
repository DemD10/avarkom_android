package ru.arkell.avarkom.presentation.main.tabs.car_accident

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_CHECK_SETTINGS
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_PERMISSIONS_REQUEST_CODE
import javax.inject.Inject

class CarAccidentServiceImpl @Inject constructor(
    private val view: CarAccidentFragment) : CarAccidentService,
    GoogleMap.OnCameraIdleListener,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks {

  override var googleMap: GoogleMap? = null
  override var fusedLocationProviderClient: FusedLocationProviderClient? = null
  private var mLocationRequest: LocationRequest? = null
  private var googleApiClient: GoogleApiClient? = null

  private val navigatorView: CarAccidentView = view

  private val defaultZoomLevel = 16.0f

  private var lastLatLng: LatLng? = null

  private fun createFusedLocationClient() {
    if (fusedLocationProviderClient == null) {
      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
          view.context.applicationContext)
    }
  }

  @SuppressLint("MissingPermission")
  override fun checkGPSOn() {

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(buildLocationRequest()!!)
        .setAlwaysShow(true)

    var result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(
        view.activity).checkLocationSettings(builder.build())

    result.addOnCompleteListener {
      try {
        var response = result.getResult(ApiException::class.java)

        if (response.locationSettingsStates.isLocationUsable) {
          buildGoogleApiClient()
        }

      } catch (apiException: ApiException) {
        when (apiException.statusCode) {
          LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
            try {
              var resolvable: ResolvableApiException = apiException as ResolvableApiException
              resolvable.startResolutionForResult(view.activity, REQUEST_CHECK_SETTINGS)

            } catch (e: IntentSender.SendIntentException) {

            } catch (e: ClassCastException) {

            }
          }
          LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
          }
        }
      }
    }
  }

  override fun checkPermissions(): Boolean = ActivityCompat.checkSelfPermission(view.activity,
      Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

  override fun startLocationPermissionRequest() {
    ActivityCompat.requestPermissions(view.activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_PERMISSIONS_REQUEST_CODE)
  }

  override fun requestPermissions(): Boolean = ActivityCompat.shouldShowRequestPermissionRationale(
      view.activity, Manifest.permission.ACCESS_FINE_LOCATION)

  override fun buildGoogleApiClient() {

    if (googleApiClient == null) {
      googleApiClient = GoogleApiClient.Builder(view.activity)
          .addApi(LocationServices.API)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .build()
    }
    if (!googleApiClient?.isConnected!!) {
      googleApiClient?.connect()
    }
  }

  override fun buildLocationRequest(): LocationRequest? {

    if (mLocationRequest == null) {
      mLocationRequest = LocationRequest.create()
      mLocationRequest?.interval = 120000
      mLocationRequest?.fastestInterval = 30000
      mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
      return mLocationRequest
    }
    return mLocationRequest
  }

  @SuppressLint("MissingPermission")
  override fun onConnected(p0: Bundle?) {

    var mLocationCallback: LocationCallback = object : LocationCallback() {
      override fun onLocationResult(p0: LocationResult?) {
        super.onLocationResult(p0)
        navigatorView.getLastLocation(
            LatLng(p0?.lastLocation!!.latitude, p0.lastLocation.longitude))
        fusedLocationProviderClient?.removeLocationUpdates(this)
      }
    }

    createFusedLocationClient()

    fusedLocationProviderClient?.lastLocation?.addOnCompleteListener { task ->
      if (task.isSuccessful && task.result != null) {
        navigatorView.getLastLocation(LatLng(task.result.latitude, task.result.longitude))
      } else {
        fusedLocationProviderClient?.requestLocationUpdates(buildLocationRequest(),
            mLocationCallback, null)
      }
    }
  }

  override fun onConnectionSuspended(p0: Int) {
  }

  override fun onConnectionFailed(p0: ConnectionResult) {
  }


  override fun onMapReady(map: GoogleMap?) {

    if (googleMap == null) {
      googleMap = map
      googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
      googleMap?.setOnCameraIdleListener(this)
    }

    if (!checkPermissions()) {
      navigatorView.requestPermissions()
    } else {
      checkGPSOn()
    }
  }


  override fun setLastLocation(latLng: LatLng) {
    this.lastLatLng = latLng
  }

  override fun getNewLocationPlacement() {
    if (googleMap?.cameraPosition?.target != lastLatLng) {
      googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(lastLatLng, defaultZoomLevel)))
    }
  }

  override fun setLocationAndMoveCamera(latLng: LatLng) {
  }

  override fun onCameraIdle() {
    navigatorView.getAddress(googleMap?.cameraPosition?.target)
  }
}
