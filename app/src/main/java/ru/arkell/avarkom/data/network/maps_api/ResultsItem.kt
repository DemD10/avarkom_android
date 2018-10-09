package ru.arkell.avarkom.data.network.maps_api

import com.google.gson.annotations.SerializedName

data class ResultsItem(
    @SerializedName("formatted_address")
    val formattedAddress: String? = null,
    val types: List<String?>? = null,
    val geometry: Geometry,
    @SerializedName("address_components")
    val addressComponents: List<AddressComponentsItem>? = null,
    val placeId: String? = null
)
