package ru.arkell.avarkom.data.network.maps_api

import com.google.gson.annotations.SerializedName

data class GeoCoderAddress(@SerializedName("formatted_address")
                       val formattedAddress: String = "")