package ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_select_address.addressSearchingCoordinator
import kotlinx.android.synthetic.main.activity_select_address.addressSearchingProgressBar
import kotlinx.android.synthetic.main.activity_select_address.backButton
import kotlinx.android.synthetic.main.activity_select_address.pickPlaceOnMapContainer
import kotlinx.android.synthetic.main.activity_select_address.placesText
import kotlinx.android.synthetic.main.activity_select_address.previousSearchesList
import kotlinx.android.synthetic.main.activity_select_address.searchResultsList
import kotlinx.android.synthetic.main.activity_select_address.voiceSearchButton
import kotlinx.android.synthetic.main.place_recent_search_item.view.countryText
import kotlinx.android.synthetic.main.place_recent_search_item.view.placesAddress
import kotlinx.android.synthetic.main.place_recent_search_item.view.recentSearchContainer
import kotlinx.android.synthetic.main.place_search_item.view.countrySearchText
import kotlinx.android.synthetic.main.place_search_item.view.placeSearchContainer
import kotlinx.android.synthetic.main.place_search_item.view.placesSeachAddress
import ru.arkell.avarkom.R
import ru.arkell.avarkom.data.network.maps_api.GeoCoderAddress
import ru.arkell.avarkom.data.network.maps_api.ResultsItem
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.extensions.displaySpeechRecognizer
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.extensions.showSnackbar
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.base.BaseRecyclerAdapter
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.di.DaggerSelectAddressScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.di.SelectAddressScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.di.SelectAddressScreenModule
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject


const val RESULT_PICK_ON_MAP = 15
const val SPEECH_REQUEST_CODE = 0

class SelectAddressActivity : BaseActivity(), SelectAddressView {
  lateinit var component: SelectAddressScreenComponent
  @Inject
  lateinit var presenter: SelectAddressPresenter
  private val placesAdapter: PlacesAdapter by lazy { PlacesAdapter() }
  private val previousSearchesAdapter: RecentSearchesAdapter by lazy { RecentSearchesAdapter() }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initComponent()
    presenter.onAttach(this)
    setContentView(R.layout.activity_select_address)
    initViewElements()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDetach()
  }

  override fun initViewElements() {
    RxTextView.textChanges(placesText).debounce(500, MILLISECONDS)
        .compose(RxHelper.applyObservableScheduler())
        .subscribe {
          if (!it.isNullOrEmpty()) presenter.getLocationFromQuery(it.toString())
          else presenter.getRecentSearches()
        }

    pickPlaceOnMapContainer.setOnClickListener {
      setResult(RESULT_PICK_ON_MAP)
      finish()
    }
    searchResultsList.adapter = placesAdapter
    previousSearchesList.adapter = previousSearchesAdapter
    backButton.setOnClickListener {
      setResult(Activity.RESULT_CANCELED)
      finish()
    }
    voiceSearchButton.setOnClickListener { displaySpeechRecognizer(this) }
    placesAdapter.onItemSelectAction.subscribe { saveAddressAndCloseScreen(it) }
    previousSearchesAdapter.onItemSelectAction.subscribe { saveAddressAndCloseScreen(it) }

    presenter.getRecentSearches()

    val searchText = intent?.extras?.getString("search_query")
    if (searchText != null) placesText.setText(searchText)
  }

  private fun saveAddressAndCloseScreen(it: ResultsItem) {
    presenter.saveSelectedAddress(it)
    val intent = Intent()
    intent.putExtra("place_string", it.formattedAddress)
    intent.putExtra(("place_latlng"), LatLng(it.geometry.location.lat, it.geometry.location.lng))
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

  override fun initComponent() {
    component = DaggerSelectAddressScreenComponent.builder()
        .persistenceComponent(getAvarkomApplication().persistenceComponent())
        .selectAddressScreenModule(SelectAddressScreenModule())
        .build()

    component.inject(this)
  }

  override fun showLoading() {
    addressSearchingProgressBar.show()
  }

  override fun hideLoading() {
    addressSearchingProgressBar.hide(true)
  }

  override fun showError() {
    addressSearchingCoordinator.showSnackbar("Search error")
  }

  override fun showAddressSuggestions(suggestions: GeoCoderAddress) {
  }

  override fun showPlaceSuggestions(address: List<ResultsItem>) {
    previousSearchesList.hide(true)
    placesAdapter.setData(address)
  }

  override fun showLocation(address: ResultsItem) {
    addressSearchingCoordinator.showSnackbar(address.formattedAddress.toString())
  }

  class PlacesAdapter : BaseRecyclerAdapter<ResultsItem>() {
    var onItemSelectAction: PublishSubject<ResultsItem> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): RecyclerViewHolder<ResultsItem> {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.place_search_item, parent,
          false)
      return ViewHolder(view)
    }

    private inner class ViewHolder(itemView: View) : RecyclerViewHolder<ResultsItem>(itemView) {
      init {
        itemView.placeSearchContainer.setOnClickListener {
          onItemSelectAction.onNext(dataItems[adapterPosition])
        }
      }

      override fun setItem(item: ResultsItem, position: Int) {
        itemView.placesSeachAddress.text = item.formattedAddress
        var additionalAddress = ""
        if (item.addressComponents != null)
          item.addressComponents.let {
            it.forEach { address ->
              additionalAddress = additionalAddress + address.shortName + ", "
            }
          }
        itemView.countrySearchText.text = additionalAddress
      }

    }
  }

  class RecentSearchesAdapter : BaseRecyclerAdapter<ResultsItem>() {
    var onItemSelectAction: PublishSubject<ResultsItem> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): RecyclerViewHolder<ResultsItem> {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.place_recent_search_item,
          parent,
          false)
      return ViewHolder(view)
    }

    private inner class ViewHolder(itemView: View) : RecyclerViewHolder<ResultsItem>(itemView) {
      init {
        itemView.recentSearchContainer.setOnClickListener {
          onItemSelectAction.onNext(dataItems[adapterPosition])
        }
      }

      override fun setItem(item: ResultsItem, position: Int) {
        itemView.placesAddress.text = item.formattedAddress
        var additionalAddress = ""
        if (item.addressComponents != null)
          item.addressComponents.let {
            it.forEach { address ->
              additionalAddress = additionalAddress + address.shortName + ", "
            }
          }
        itemView.countryText.text = additionalAddress
      }

    }
  }

  override fun showRecentSearches(searched: List<ResultsItem>) {
    previousSearchesAdapter.setData(searched)
  }

  // This callback is invoked when the Speech Recognizer returns.
  // This is where you process the intent and extract the speech text from the intent.
  override fun onActivityResult(requestCode: Int, resultCode: Int,
      data: Intent?) {
    if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val results = data?.getStringArrayListExtra(
          RecognizerIntent.EXTRA_RESULTS)
      val spokenText = results?.firstOrNull()
      if (spokenText != null) {
        placesText.setText(spokenText)
        presenter.getLocationFromQuery(spokenText)
      }
    }
    super.onActivityResult(requestCode, resultCode, data)
  }
}
