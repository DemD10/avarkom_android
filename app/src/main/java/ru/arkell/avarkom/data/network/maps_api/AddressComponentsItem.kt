package ru.arkell.avarkom.data.network.maps_api

import com.google.gson.annotations.SerializedName
import ru.arkell.avarkom.extensions.default


data class AddressComponentsItem(
    var types: List<String> = mutableListOf(),
    @SerializedName("short_name")
    var shortName: String = String.default(),
    @SerializedName("long_name")
    var longName: String = String.default()
)
