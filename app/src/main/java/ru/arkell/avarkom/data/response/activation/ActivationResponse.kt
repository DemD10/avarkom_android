package ru.arkell.avarkom.data.response.activation

import com.google.gson.annotations.SerializedName
import ru.arkell.avarkom.extensions.default

data class ActivationResponse(
	@SerializedName("msg")
	val msg: String = String.default(),
	@SerializedName("status")
	val status: Int = 0
)