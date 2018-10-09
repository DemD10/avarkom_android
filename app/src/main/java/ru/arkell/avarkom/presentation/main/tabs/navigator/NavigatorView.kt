package ru.arkell.avarkom.presentation.main.tabs.navigator

import android.view.View
import com.google.android.gms.maps.model.LatLng
import ru.arkell.avarkom.presentation.base.BaseLCEView

interface NavigatorView : BaseLCEView {
  fun requestPermissions()
  fun showAddress(address: String, isFromPicker: Boolean)
  fun showDestinationAddress(address: String, isFromPicker: Boolean)
  fun showAddressLoading()
  fun hideAddressLoading()
  fun showPermissionInfoView()
  fun expandMapView(isViewExpanded: Boolean)
  fun getAddress(latLng: LatLng?)
  fun getLastLocation(latLng: LatLng?)
  fun showSnackBar(listener: View.OnClickListener? = null)
  fun showRoute(location: LatLng, destination: LatLng)
  fun activatePickerSelection()
  fun donePickerSelection()
  fun switchDestinations()
  fun showBottomNavigatorView()
  fun hideBottomNavigatorView()
}