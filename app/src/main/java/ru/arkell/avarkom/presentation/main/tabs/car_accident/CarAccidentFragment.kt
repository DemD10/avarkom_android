package ru.arkell.avarkom.presentation.main.tabs.car_accident

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.Tab
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_car_accident.accidentAddressContainer
import kotlinx.android.synthetic.main.fragment_car_accident.accidentTabLayout
import kotlinx.android.synthetic.main.fragment_car_accident.accidentVoiceSearch
import kotlinx.android.synthetic.main.fragment_car_accident.addressProgress
import kotlinx.android.synthetic.main.fragment_car_accident.carAccidentActionButton
import kotlinx.android.synthetic.main.fragment_car_accident.placesText
import ru.arkell.avarkom.R
import ru.arkell.avarkom.R.string
import ru.arkell.avarkom.extensions.displaySpeechRecognizer
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.presentation.base.BaseFragment
import ru.arkell.avarkom.presentation.main.tabs.car_accident.di.CarAccidentScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.car_accident.di.CarAccidentScreenModule
import ru.arkell.avarkom.presentation.main.tabs.car_accident.di.DaggerCarAccidentScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SPEECH_REQUEST_CODE
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SelectAddressActivity
import ru.arkell.avarkom.presentation.main.tabs.navigator.services.REQUEST_CHECK_SETTINGS
import javax.inject.Inject


const val REQUEST_CODE_ACCIDENT_SEARCH = 12

class CarAccidentFragment : BaseFragment(), CarAccidentView, OnMapReadyCallback {
  private lateinit var component: CarAccidentScreenComponent

  @Inject
  lateinit var presenter: CarAccidentPresenter

  @Inject
  lateinit var mapService: CarAccidentService

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_car_accident, container, false)
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
    val tab1 = accidentTabLayout.newTab()
    tab1.text = getString(string.call_commissioner)
    val tab2 = accidentTabLayout.newTab()
    tab2.text = getString(string.notify_accident)

    accidentTabLayout.addTab(tab1)
    accidentTabLayout.addTab(tab2)
    accidentTabLayout.getTabAt(0)?.select()

    accidentTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabReselected(tab: Tab?) {
      }

      override fun onTabUnselected(tab: Tab?) {
      }

      override fun onTabSelected(tab: Tab) {
        when (tab.position) {
          0 -> showComissarCallView()
          1 -> showInformCallView()
        }
      }
    })


    val mapFragment = activity.fragmentManager
        .findFragmentById(R.id.mapAccidentView) as MapFragment
    mapFragment.getMapAsync(this)

    accidentAddressContainer.setOnClickListener {
      val intent = Intent(activity, SelectAddressActivity::class.java)
      startActivityForResult(intent, REQUEST_CODE_ACCIDENT_SEARCH)
    }
    accidentVoiceSearch.setOnClickListener { displaySpeechRecognizer(this) }
  }

  override fun initComponent() {
    component = DaggerCarAccidentScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .carAccidentScreenModule(CarAccidentScreenModule(this))
        .build()

    component.inject(this)
  }

  override fun showLoading() {
    addressProgress.show()
  }

  override fun hideLoading() {
    addressProgress.hide(false)
  }

  override fun showError() {
  }

  override fun getAddress(latLng: LatLng?) {
    presenter.getAddress(latLng!!)
  }

  override fun getLastLocation(latLng: LatLng?) {
    presenter.getLastLocation(latLng!!)
  }

  override fun requestPermissions() {
    if (mapService.requestPermissions()) {
      showPermissionInfoView()
    } else {
      mapService.startLocationPermissionRequest()
    }
  }

  override fun showAddress(address: String) {
    placesText.text = address
  }

  override fun onMapReady(p0: GoogleMap?) {
    mapService.onMapReady(p0)
  }

  fun showPermissionInfoView() {

  }

  override fun showComissarCallView() {
    carAccidentActionButton.text = "Вызвать"
    carAccidentActionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_car_call, 0, 0,
        0)

  }

  override fun showInformCallView() {
    carAccidentActionButton.text = "Сообщить"
    carAccidentActionButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rupor_call, 0, 0,
        0)
  }

  override fun showSnackBar(listener: OnClickListener?) {
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        REQUEST_CHECK_SETTINGS -> {
          when (resultCode) {
            Activity.RESULT_OK -> {
              mapService.buildGoogleApiClient()
            }
            Activity.RESULT_CANCELED -> this.activity.finish()
          }
        }
        REQUEST_CODE_ACCIDENT_SEARCH -> {
          presenter.getAddress(data?.getParcelableExtra("place_latlng")!!)
          mapService.getNewLocationPlacement()
          showAddress(data?.getStringExtra("place_string")!!)
        }
        SPEECH_REQUEST_CODE -> {
          val results = data?.getStringArrayListExtra(
              RecognizerIntent.EXTRA_RESULTS)
          val spokenText = results?.firstOrNull()
          if (spokenText != null) {
            val intent = Intent(activity, SelectAddressActivity::class.java)
            intent.putExtra("search_query", spokenText)
            startActivityForResult(intent, REQUEST_CODE_ACCIDENT_SEARCH)
          }
        }
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    presenter.onRequestPermissionsResult(activity, requestCode, permissions, grantResults)
  }

}