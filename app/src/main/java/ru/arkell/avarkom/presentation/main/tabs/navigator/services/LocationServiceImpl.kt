package ru.arkell.avarkom.presentation.main.tabs.navigator.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.directions.route.AbstractRouting
import com.directions.route.Route
import com.directions.route.RouteException
import com.directions.route.Routing
import com.directions.route.RoutingListener
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import ru.arkell.avarkom.R
import ru.arkell.avarkom.data.network.directions_api.DESTINATIONS_KEY
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorFragment
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorView
import java.util.ArrayList
import javax.inject.Inject

const val REQUEST_PERMISSIONS_REQUEST_CODE = 1
const val REQUEST_CHECK_SETTINGS = 0x1

class LocationServiceImpl @Inject constructor(
    private val view: NavigatorFragment) : LocationService,
    GoogleMap.OnCameraIdleListener,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks, RoutingListener {

  override var googleMap: GoogleMap? = null
  override var fusedLocationProviderClient: FusedLocationProviderClient? = null
  private var mLocationRequest: LocationRequest? = null
  private var googleApiClient: GoogleApiClient? = null
  private var polylines: MutableList<Polyline> = mutableListOf()
  var isSelectionActive = false

  private val navigatorView: NavigatorView = view

  private val defaultZoomLevel = 16.0f

  private var lastLatLng: LatLng? = null

  private fun createFusedLocationClient() {
    if (fusedLocationProviderClient == null) {
      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
          view.context.applicationContext)
    }
  }

  private var locationLatLng: LatLng? = null
  private var destinationLatLng: LatLng? = null

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

    googleMap?.setOnMapClickListener {
      val bottomSheet = NavigatorBottomDialog()
      bottomSheet.show(view.fragmentManager, bottomSheet.tag)
      bottomSheet.isCancelable = true
    }

  }


  override fun setLastLocation(latLng: LatLng) {
    this.lastLatLng = latLng
  }

  override fun renderMapMarker() {
    val img = BitmapFactory.decodeResource(view.context.resources, R.drawable.ic_blue_arrow)
    val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
        Bitmap.createScaledBitmap(img, 88, 132, false)
    )

    if (googleMap?.cameraPosition?.target != lastLatLng) {
      googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(
          CameraPosition.fromLatLngZoom(lastLatLng, defaultZoomLevel)))
      googleMap?.addMarker(MarkerOptions()
          .icon(bitmapDescriptor)
          .position(lastLatLng!!))
    }
  }

  override fun onCameraIdle() {
    if (isSelectionActive) navigatorView.getAddress(googleMap?.cameraPosition?.target)
  }

  override fun onRoutingCancelled() {
  }

  override fun onRoutingStart() {
    view.showLoading()
  }

  override fun onRoutingFailure(p0: RouteException?) {
  }

  override fun onRoutingSuccess(route: ArrayList<Route>?, i: Int) {
    view.hideLoading()

    val center = CameraUpdateFactory.newLatLng(locationLatLng)
    val zoom = CameraUpdateFactory.zoomTo(16f)
    googleMap?.clear()

    googleMap?.moveCamera(center)

    if (polylines.size > 0) {
      for (poly in polylines) {
        poly.remove()
      }
    }

    polylines = ArrayList<Polyline>()

    val polyOptions = PolylineOptions()
    polyOptions.width((10 * 2).toFloat())
    polyOptions.addAll(route?.first()?.points)
    polyOptions.color(view.context.resources.getColor(R.color.colorAccent))
    val polyline = googleMap?.addPolyline(polyOptions)
    polylines.add(polyline!!)


    // Start marker
    var options = MarkerOptions()
    options.position(locationLatLng!!)
    googleMap?.addMarker(options)

    // End marker
    options = MarkerOptions()
    options.position(destinationLatLng!!)
    googleMap?.addMarker(options)

    view.showBottomNavigatorView()
  }

  override fun createRoute(startLatLng: LatLng, destLatLng: LatLng) {
    locationLatLng = startLatLng
    destinationLatLng = destLatLng

    Log.e("LATLNG",
        ("service " + locationLatLng.toString() + "/" + destinationLatLng.toString())
    )

    val routing = Routing.Builder()
        .travelMode(AbstractRouting.TravelMode.DRIVING)
        .withListener(this)
        .alternativeRoutes(true)
        .key(DESTINATIONS_KEY)
        .waypoints(startLatLng, destLatLng)
        .build()
    routing.execute()
  }

  override fun zoomIn() {
    googleMap?.animateCamera(CameraUpdateFactory.zoomTo(googleMap?.cameraPosition?.zoom?.plus(1)!!))
  }

  override fun zoomOut() {
    googleMap?.animateCamera(
        CameraUpdateFactory.zoomTo(googleMap?.cameraPosition?.zoom?.minus(1)!!))
  }

  override fun activateSelection() {
    isSelectionActive = true
  }

  override fun deactivateSelection() {
    isSelectionActive = false
  }
}
