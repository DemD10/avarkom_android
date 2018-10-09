package ru.arkell.avarkom.data.response.news

import com.google.gson.annotations.SerializedName

data class Region(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)