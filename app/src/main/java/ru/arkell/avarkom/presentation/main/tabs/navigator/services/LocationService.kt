package ru.arkell.avarkom.presentation.main.tabs.navigator.services

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

interface LocationService {

    var googleMap: GoogleMap?
    var fusedLocationProviderClient: FusedLocationProviderClient?

    fun checkGPSOn()
    fun createRoute(startLatLng: LatLng, destLatLng: LatLng)
    fun buildGoogleApiClient()
    fun buildLocationRequest(): LocationRequest?
    fun renderMapMarker()
    fun setLastLocation(latLng: LatLng)
    fun checkPermissions(): Boolean
    fun startLocationPermissionRequest()
    fun requestPermissions(): Boolean
    fun onMapReady(map: GoogleMap?)
    fun zoomIn()
    fun zoomOut()
    fun activateSelection()
    fun deactivateSelection()
}