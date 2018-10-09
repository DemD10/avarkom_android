package ru.arkell.avarkom.data.response.news

import com.google.gson.annotations.SerializedName
import ru.arkell.avarkom.extensions.default

data class NewsItem(

	@SerializedName("image_link")
	var imageLink: String = String.default(),

	@SerializedName("created_at")
	var createdAt: String = String.default(),

	@SerializedName("id")
	var id: Int = 0,

	@SerializedName("text")
	var text: String? = null,

	@SerializedName("title")
	var title: String = String.default(),

	@SerializedName("region")
	var region: Region = Region()
)