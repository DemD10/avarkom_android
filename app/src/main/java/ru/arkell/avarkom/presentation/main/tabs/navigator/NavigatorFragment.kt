package ru.arkell.avarkom.presentation.main.tabs.navigator

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.bottom_sheet_navigator.createRouteButton
import kotlinx.android.synthetic.main.fragment_navigator.decreaseZoomButton
import kotlinx.android.synthetic.main.fragment_navigator.destinationPlaceContainer
import kotlinx.android.synthetic.main.fragment_navigator.destinationText
import kotlinx.android.synthetic.main.fragment_navigator.increaseZoomButton
import kotlinx.android.synthetic.main.fragment_navigator.mapContainer
import kotlinx.android.synthetic.main.fragment_navigator.myLocationContainerText
import kotlinx.android.synthetic.main.fragment_navigator.myLocationText
import kotlinx.android.synthetic.main.fragment_navigator.navigatorAppbar
import kotlinx.android.synthetic.main.fragment_navigator.navigatorCoordinator
import kotlinx.android.synthetic.main.fragment_navigator.navigatorMapPicker
import kotlinx.android.synthetic.main.fragment_navigator.navigatorProgressBar
import kotlinx.android.synthetic.main.fragment_navigator.pickerConfirmationActionButton
import kotlinx.android.synthetic.main.fragment_navigator.placesNavigatorText
import kotlinx.android.synthetic.main.fragment_navigator.routeBottomSheet
import kotlinx.android.synthetic.main.fragment_navigator.switchDestinationButton
import ru.arkell.avarkom.R
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.presentation.base.BaseFragment
import ru.arkell.avarkom.presentation.main.tabs.navigator.di.DaggerNavigatorScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.navigator.di.NavigatorScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.navigator.di.NavigatorScreenModule
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.RESULT_PICK_ON_MAP
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SelectAddressActivity
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.LocationService
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_CHECK_SETTINGS
import javax.inject.Inject


const val REQUEST_CODE_MY_PLACE_SEARCH = 10
const val REQUEST_CODE_DESTINATION_PLACE_SEARCH = 11

class NavigatorFragment : BaseFragment(), NavigatorView, OnMapReadyCallback {
  private lateinit var component: NavigatorScreenComponent

  @Inject
  lateinit var locationService: LocationService
  @Inject
  lateinit var presenter: NavigatorPresenter

  private var isMyLocationSearchEnabled: Boolean = true

  private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


  override fun initComponent() {
    component = DaggerNavigatorScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .navigatorScreenModule(NavigatorScreenModule(this))
        .build()

    component.inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_navigator, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.onAttach(this)
    initViewElements()
  }

  override fun onDetach() {
    super.onDetach()
    presenter.onDetach()
  }

  override fun initViewElements() {
    bottomSheetBehavior = BottomSheetBehavior.from(routeBottomSheet)
    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN)
          bottomSheet.post({ bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED })
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    })


    val mapFragment = activity.fragmentManager
        .findFragmentById(R.id.mapView) as MapFragment
    mapFragment.getMapAsync(this)

    myLocationContainerText.setOnClickListener {
      isMyLocationSearchEnabled = true
      val intent = Intent(activity, SelectAddressActivity::class.java)
      startActivityForResult(intent, REQUEST_CODE_MY_PLACE_SEARCH)
    }

    destinationPlaceContainer.setOnClickListener {
      isMyLocationSearchEnabled = false
      val intent = Intent(activity, SelectAddressActivity::class.java)
      startActivityForResult(intent, REQUEST_CODE_DESTINATION_PLACE_SEARCH)
    }

    switchDestinationButton.setOnClickListener { presenter.switchDestinations() }

    increaseZoomButton.setOnClickListener { locationService.zoomIn() }
    decreaseZoomButton.setOnClickListener { locationService.zoomOut() }
    pickerConfirmationActionButton.setOnClickListener { donePickerSelection() }
  }

  override fun showLoading() {
    mapContainer.isEnabled = false
    navigatorProgressBar.show()
  }

  override fun hideLoading() {
    mapContainer.isEnabled = true
    navigatorProgressBar.hide(true)
  }

  override fun showError() {
  }

  override fun onMapReady(p0: GoogleMap?) {
    locationService.onMapReady(p0)
  }

  override fun getAddress(latLng: LatLng?) {
    var isFromPicker: Boolean = false
    if (pickerConfirmationActionButton.visibility == View.VISIBLE) {
      isFromPicker = true
      if (isMyLocationSearchEnabled)
        presenter.getAddress(REQUEST_CODE_MY_PLACE_SEARCH, isFromPicker, latLng!!)
      else presenter.getAddress(REQUEST_CODE_DESTINATION_PLACE_SEARCH, isFromPicker, latLng!!)
    }

  }

  override fun getLastLocation(latLng: LatLng?) {
    presenter.getLastLocation(latLng!!)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    presenter.onRequestPermissionsResult(activity, requestCode, permissions, grantResults)
  }

  override fun requestPermissions() {
    if (locationService.requestPermissions()) {
      showPermissionInfoView()
    } else {
      locationService.startLocationPermissionRequest()
    }
  }

  override fun showAddress(address: String, isFromPicker: Boolean) {
    if (isFromPicker) {
      placesNavigatorText.text = address
      return
    }
    myLocationText.text = address
  }

  override fun showDestinationAddress(address: String, isFromPicker: Boolean) {
    if (isFromPicker) {
      placesNavigatorText.text = address
      return
    }
    destinationText.text = address
  }

  override fun showAddressLoading() {
  }

  override fun hideAddressLoading() {
  }

  override fun showPermissionInfoView() {
    val builder = AlertDialog.Builder(this.context)
    builder.setTitle(getString(R.string.location_permission_info))
    builder.setPositiveButton(android.R.string.ok, { dialog, id ->
      locationService.startLocationPermissionRequest()
    })

    builder.setNegativeButton(android.R.string.cancel, { dialog, id ->
      showSnackBar(View.OnClickListener { locationService.startLocationPermissionRequest() })
      dialog.dismiss()
    })

    val dialog = builder.create()
    dialog.show()
  }

  override fun expandMapView(isViewExpanded: Boolean) {
  }

  @SuppressLint("MissingPermission")
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode == RESULT_OK) {
      when (requestCode) {
        REQUEST_CHECK_SETTINGS -> {
          when (resultCode) {
            Activity.RESULT_OK -> {
              locationService.buildGoogleApiClient()
            }
            Activity.RESULT_CANCELED -> this.activity.finish()
          }
        }
        REQUEST_CODE_MY_PLACE_SEARCH -> {
          presenter.locationLatLng = data?.getParcelableExtra("place_latlng")!!
          showAddress(data.getStringExtra("place_string")!!, false)
          presenter.checkIfDestinationsFilled()
        }
        REQUEST_CODE_DESTINATION_PLACE_SEARCH -> {
          presenter.destinationLatLng = data?.getParcelableExtra("place_latlng")!!
          showDestinationAddress(
              data.getStringExtra("place_string")!!, false)
          presenter.checkIfDestinationsFilled()
        }
      }
    } else if (resultCode == RESULT_PICK_ON_MAP) {
      when (requestCode) {
        REQUEST_CODE_MY_PLACE_SEARCH -> {
          // show location picker for my place
          activatePickerSelection()
        }
        REQUEST_CODE_DESTINATION_PLACE_SEARCH -> {
          // show location picker for destination place
          activatePickerSelection()
        }
      }
    }
  }

  override fun activatePickerSelection() {
    locationService.activateSelection()
    navigatorMapPicker.show()
    navigatorAppbar.hide(true)
    val params = mapContainer.layoutParams as CoordinatorLayout.LayoutParams
    params.behavior = null

  }

  override fun donePickerSelection() {
    navigatorMapPicker.hide(true)
    navigatorAppbar.show()
    val params = mapContainer.layoutParams as CoordinatorLayout.LayoutParams
    params.behavior = AppBarLayout.ScrollingViewBehavior()
    mapContainer.requestLayout()
    if (isMyLocationSearchEnabled)
      showAddress(placesNavigatorText.text.toString(), false)
    else showDestinationAddress(placesNavigatorText.text.toString(), false)
    presenter.checkIfDestinationsFilled()
    locationService.deactivateSelection()
  }

  override fun switchDestinations() {
    val tempDestination = destinationText.text.toString()
    destinationText.text = myLocationText.text
    myLocationText.text = tempDestination
    presenter.checkIfDestinationsFilled()
  }

  override fun showSnackBar(listener: View.OnClickListener?) {
    val snackBar = Snackbar.make(navigatorCoordinator, getString(R.string.snackbar_location_info),
        BaseTransientBottomBar.LENGTH_INDEFINITE)
    if (listener != null) {
      snackBar.setAction(getString(R.string.allow), listener)
    }
    snackBar.show()
  }

  override fun showBottomNavigatorView() {
    routeBottomSheet.visibility = View.VISIBLE
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    createRouteButton.setOnClickListener {
      val intent = Intent(android.content.Intent.ACTION_VIEW,
          Uri.parse(
              "http://maps.google.com/maps?saddr=${presenter.locationLatLng?.latitude},${presenter.locationLatLng?.longitude}" +
                  "&daddr=${presenter.destinationLatLng?.latitude},${presenter.destinationLatLng?.longitude}"))
      startActivity(intent)
    }
  }

  override fun hideBottomNavigatorView() {
    routeBottomSheet.visibility = View.GONE
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
  }

  override fun showRoute(location: LatLng, destination: LatLng) {
    Log.e("LATLNG",
        ("route " + location.toString() + "/" + destination.toString())
    )
    locationService.createRoute(location, destination)
  }
}