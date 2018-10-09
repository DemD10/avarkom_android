package ru.arkell.avarkom.presentation.main.tabs.car_accident

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface CarAccidentService {

  var googleMap: GoogleMap?
  var fusedLocationProviderClient: FusedLocationProviderClient?
  fun checkGPSOn()
  fun buildGoogleApiClient()
  fun buildLocationRequest(): LocationRequest?
  fun getNewLocationPlacement()
  fun setLastLocation(latLng: LatLng)
  fun setLocationAndMoveCamera(latLng: LatLng)
  fun checkPermissions(): Boolean
  fun startLocationPermissionRequest()
  fun requestPermissions(): Boolean
  fun onMapReady(map: GoogleMap?)
}