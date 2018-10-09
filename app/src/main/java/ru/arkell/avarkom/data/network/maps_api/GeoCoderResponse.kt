package ru.arkell.avarkom.data.network.maps_api

data class GeoCoderResponse(val results: List<GeoCoderAddress>?,
                            val status: String = "")