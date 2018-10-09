package ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address

import ru.arkell.avarkom.data.network.maps_api.GeoCoderAddress
import ru.arkell.avarkom.data.network.maps_api.ResultsItem
import ru.arkell.avarkom.presentation.base.BaseLCEView

interface SelectAddressView : BaseLCEView {
  fun showRecentSearches(searched: List<ResultsItem>)
  fun showAddressSuggestions(suggestions: GeoCoderAddress)
  fun showPlaceSuggestions(address: List<ResultsItem>)
  fun showLocation(address: ResultsItem)
}