package ru.arkell.avarkom.presentation.main.tabs.car_accident;

import android.view.View
import com.google.android.gms.maps.model.LatLng
import ru.arkell.avarkom.presentation.base.BaseLCEView

interface CarAccidentView : BaseLCEView {
  fun getAddress(latLng: LatLng?)
  fun getLastLocation(latLng: LatLng?)
  fun requestPermissions()
  fun showAddress(address: String)
  fun showSnackBar(listener: View.OnClickListener? = null)
  fun showComissarCallView()
  fun showInformCallView()
}